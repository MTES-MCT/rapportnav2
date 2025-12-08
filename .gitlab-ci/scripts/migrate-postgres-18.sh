#!/bin/bash
#
# PostgreSQL Major Version Migration Script
# Migrates from any PostgreSQL version to 18.x
#
# Usage: ./migrate-postgres-18.sh [options]
#
# Options:
#   --backup-only     Only create a backup, don't perform migration
#   --restore-only    Only restore from a backup file (requires --backup-file)
#   --backup-file     Path to backup file for restore
#   --skip-confirm    Skip confirmation prompts (use with caution)
#   --help            Show this help message
#

set -e

# Configuration
COMPOSE_FILE="${COMPOSE_FILE:-docker-compose.yml}"
DB_CONTAINER="${DB_CONTAINER:-rapportnav_db}"
DB_USER="${DB_USER:-postgres}"
VOLUME_NAME="${VOLUME_NAME:-rapportnavdb}"
BACKUP_DIR="${BACKUP_DIR:-./backups}"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="${BACKUP_DIR}/postgres_backup_${TIMESTAMP}.sql"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Show help
show_help() {
    head -20 "$0" | tail -15
    exit 0
}

# Check if container is running
check_container_running() {
    if ! docker ps --format '{{.Names}}' | grep -q "^${DB_CONTAINER}$"; then
        return 1
    fi
    return 0
}

# Get current PostgreSQL version
get_postgres_version() {
    if check_container_running; then
        docker exec "${DB_CONTAINER}" psql -U "${DB_USER}" -t -c "SELECT version();" 2>/dev/null | head -1 | awk '{print $2}'
    else
        echo "unknown (container not running)"
    fi
}

# Create backup
create_backup() {
    log_info "Creating backup directory: ${BACKUP_DIR}"
    mkdir -p "${BACKUP_DIR}"

    log_info "Creating database backup: ${BACKUP_FILE}"
    log_info "This may take a while depending on database size..."

    if ! docker exec "${DB_CONTAINER}" pg_dumpall -U "${DB_USER}" > "${BACKUP_FILE}"; then
        log_error "Backup failed!"
        exit 1
    fi

    BACKUP_SIZE=$(du -h "${BACKUP_FILE}" | cut -f1)
    log_info "Backup created successfully: ${BACKUP_FILE} (${BACKUP_SIZE})"

    # Verify backup is not empty
    if [ ! -s "${BACKUP_FILE}" ]; then
        log_error "Backup file is empty! Aborting."
        exit 1
    fi

    # Quick validation - check for PostgreSQL dump markers
    if ! grep -q "PostgreSQL database dump" "${BACKUP_FILE}"; then
        log_warn "Backup file doesn't contain expected PostgreSQL dump markers. Please verify manually."
    fi
}

# Stop containers
stop_containers() {
    log_info "Stopping containers..."
    docker-compose -f "${COMPOSE_FILE}" down
    log_info "Containers stopped."
}

# Remove old volume
remove_volume() {
    log_info "Removing old PostgreSQL volume: ${VOLUME_NAME}"

    if docker volume ls -q | grep -q "^${VOLUME_NAME}$"; then
        docker volume rm "${VOLUME_NAME}"
        log_info "Volume removed."
    else
        log_warn "Volume ${VOLUME_NAME} not found, skipping removal."
    fi
}

# Start containers
start_containers() {
    log_info "Starting containers with new PostgreSQL version..."
    docker-compose -f "${COMPOSE_FILE}" up -d

    log_info "Waiting for PostgreSQL to be ready..."
    local retries=30
    while [ $retries -gt 0 ]; do
        if docker exec "${DB_CONTAINER}" pg_isready -U "${DB_USER}" >/dev/null 2>&1; then
            log_info "PostgreSQL is ready."
            return 0
        fi
        retries=$((retries - 1))
        sleep 2
    done

    log_error "PostgreSQL failed to start within timeout."
    exit 1
}

# Restore from backup
restore_backup() {
    local backup_to_restore="$1"

    if [ ! -f "${backup_to_restore}" ]; then
        log_error "Backup file not found: ${backup_to_restore}"
        exit 1
    fi

    log_info "Restoring database from: ${backup_to_restore}"
    log_info "This may take a while depending on database size..."

    # Use cat to pipe the backup to psql inside the container
    if ! cat "${backup_to_restore}" | docker exec -i "${DB_CONTAINER}" psql -U "${DB_USER}" >/dev/null 2>&1; then
        log_error "Restore failed!"
        exit 1
    fi

    log_info "Database restored successfully."
}

# Verify migration
verify_migration() {
    log_info "Verifying migration..."

    local new_version
    new_version=$(get_postgres_version)
    log_info "PostgreSQL version: ${new_version}"

    # Check if version is 18.x
    if [[ "${new_version}" == 18.* ]]; then
        log_info "Version check passed: Running PostgreSQL 18.x"
    else
        log_warn "Expected PostgreSQL 18.x but got: ${new_version}"
    fi

    # Test database connection and basic query
    if docker exec "${DB_CONTAINER}" psql -U "${DB_USER}" -c "SELECT 1;" >/dev/null 2>&1; then
        log_info "Database connection test: PASSED"
    else
        log_error "Database connection test: FAILED"
        exit 1
    fi

    # List databases
    log_info "Databases after migration:"
    docker exec "${DB_CONTAINER}" psql -U "${DB_USER}" -c "\l"
}

# Confirmation prompt
confirm() {
    local message="$1"
    if [ "${SKIP_CONFIRM}" = "true" ]; then
        return 0
    fi

    echo -e "${YELLOW}${message}${NC}"
    read -p "Continue? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        log_info "Operation cancelled by user."
        exit 0
    fi
}

# Main migration function
do_migration() {
    log_info "============================================"
    log_info "PostgreSQL Migration Script"
    log_info "============================================"

    # Check if container is running
    if ! check_container_running; then
        log_error "Database container '${DB_CONTAINER}' is not running."
        log_error "Please start the containers first: docker-compose up -d"
        exit 1
    fi

    # Show current version
    local current_version
    current_version=$(get_postgres_version)
    log_info "Current PostgreSQL version: ${current_version}"
    log_info "Target PostgreSQL version: 18.x"

    # Warning
    echo ""
    log_warn "============================================"
    log_warn "WARNING: This will perform the following:"
    log_warn "  1. Create a full database backup"
    log_warn "  2. Stop all containers"
    log_warn "  3. DELETE the PostgreSQL data volume"
    log_warn "  4. Start containers (with new PG version)"
    log_warn "  5. Restore database from backup"
    log_warn "============================================"
    echo ""

    confirm "This operation will cause downtime. Are you sure?"

    # Step 1: Create backup
    log_info ""
    log_info "=== Step 1/5: Creating backup ==="
    create_backup

    confirm "Backup created. Proceed to stop containers?"

    # Step 2: Stop containers
    log_info ""
    log_info "=== Step 2/5: Stopping containers ==="
    stop_containers

    confirm "Containers stopped. Proceed to remove volume? THIS IS IRREVERSIBLE!"

    # Step 3: Remove old volume
    log_info ""
    log_info "=== Step 3/5: Removing old volume ==="
    remove_volume

    # Step 4: Start containers
    log_info ""
    log_info "=== Step 4/5: Starting containers ==="
    start_containers

    # Step 5: Restore backup
    log_info ""
    log_info "=== Step 5/5: Restoring backup ==="
    restore_backup "${BACKUP_FILE}"

    # Verify
    log_info ""
    log_info "=== Verification ==="
    verify_migration

    log_info ""
    log_info "============================================"
    log_info "Migration completed successfully!"
    log_info "Backup file preserved at: ${BACKUP_FILE}"
    log_info "============================================"
}

# Parse arguments
BACKUP_ONLY=false
RESTORE_ONLY=false
RESTORE_FILE=""
SKIP_CONFIRM=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --backup-only)
            BACKUP_ONLY=true
            shift
            ;;
        --restore-only)
            RESTORE_ONLY=true
            shift
            ;;
        --backup-file)
            RESTORE_FILE="$2"
            shift 2
            ;;
        --skip-confirm)
            SKIP_CONFIRM=true
            shift
            ;;
        --help)
            show_help
            ;;
        *)
            log_error "Unknown option: $1"
            show_help
            ;;
    esac
done

# Execute based on options
if [ "${BACKUP_ONLY}" = "true" ]; then
    if ! check_container_running; then
        log_error "Database container '${DB_CONTAINER}' is not running."
        exit 1
    fi
    create_backup
elif [ "${RESTORE_ONLY}" = "true" ]; then
    if [ -z "${RESTORE_FILE}" ]; then
        log_error "--restore-only requires --backup-file <path>"
        exit 1
    fi
    if ! check_container_running; then
        log_error "Database container '${DB_CONTAINER}' is not running."
        exit 1
    fi
    restore_backup "${RESTORE_FILE}"
    verify_migration
else
    do_migration
fi
