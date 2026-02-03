package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.JtsGeometryDeserializer
import fr.gouv.dgampa.rapportnav.config.JtsGeometrySerializer
import fr.gouv.dgampa.rapportnav.config.JtsMultiPolygonDeserializer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.geojson.GeoJsonReader
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.module.SimpleModule

class JtsGeometryDeserializerTest {

    private val geometryFactory = GeometryFactory()

    private fun createMapper(): JsonMapper {
        val module = SimpleModule()
            .addDeserializer(org.locationtech.jts.geom.Geometry::class.java, JtsGeometryDeserializer())
            .addSerializer(org.locationtech.jts.geom.Geometry::class.java, JtsGeometrySerializer())
            .addDeserializer(MultiPolygon::class.java, JtsMultiPolygonDeserializer())
        return JsonMapper.builder().addModule(module).build()
    }

    @Test
    fun `should deserialize GeoJSON Point`() {
        val geoJson = """{"type":"Point","coordinates":[1.0,2.0]}"""
        val mapper = createMapper()

        val geometry = mapper.readValue(geoJson, org.locationtech.jts.geom.Geometry::class.java)

        assertNotNull(geometry)
        assertTrue(geometry is Point)
        val point = geometry as Point
        assertEquals(1.0, point.x, 0.0001)
        assertEquals(2.0, point.y, 0.0001)
    }

    @Test
    fun `should deserialize GeoJSON Polygon`() {
        val geoJson = """{"type":"Polygon","coordinates":[[[0,0],[1,0],[1,1],[0,1],[0,0]]]}"""
        val mapper = createMapper()

        val geometry = mapper.readValue(geoJson, org.locationtech.jts.geom.Geometry::class.java)

        assertNotNull(geometry)
        assertTrue(geometry is Polygon)
    }

    @Test
    fun `should deserialize GeoJSON MultiPolygon`() {
        val geoJson = """{"type":"MultiPolygon","coordinates":[[[[0,0],[1,0],[1,1],[0,1],[0,0]]]]}"""
        val mapper = createMapper()

        val geometry = mapper.readValue(geoJson, MultiPolygon::class.java)

        assertNotNull(geometry)
        assertTrue(geometry is MultiPolygon)
    }

    @Test
    fun `should promote Polygon to MultiPolygon when deserializing`() {
        val geoJson = """{"type":"Polygon","coordinates":[[[0,0],[1,0],[1,1],[0,1],[0,0]]]}"""
        val mapper = createMapper()

        val geometry = mapper.readValue(geoJson, MultiPolygon::class.java)

        assertNotNull(geometry)
        assertTrue(geometry is MultiPolygon)
        assertEquals(1, geometry.numGeometries)
    }

    @Test
    fun `should serialize Geometry to GeoJSON`() {
        val point = geometryFactory.createPoint(Coordinate(1.0, 2.0))
        val mapper = createMapper()

        val geoJson = mapper.writeValueAsString(point)

        assertNotNull(geoJson)
        assertTrue(geoJson.contains("Point"))
        assertTrue(geoJson.contains("coordinates"))
    }

    @Test
    fun `should throw exception for invalid GeoJSON`() {
        val invalidGeoJson = """{"type":"InvalidType","coordinates":[1.0,2.0]}"""
        val mapper = createMapper()

        assertThrows(Exception::class.java) {
            mapper.readValue(invalidGeoJson, org.locationtech.jts.geom.Geometry::class.java)
        }
    }

    @Test
    fun `should throw exception when MultiPolygon deserializer receives Point`() {
        val pointGeoJson = """{"type":"Point","coordinates":[1.0,2.0]}"""
        val mapper = createMapper()

        assertThrows(Exception::class.java) {
            mapper.readValue(pointGeoJson, MultiPolygon::class.java)
        }
    }
}