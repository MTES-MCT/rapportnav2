package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color

/*
 * Shape primitives for pdfbox documents.
 * Coordinates are in PDF points, origin at the bottom-left of the page.
 */

/** Draws [image] scaled to [width], keeping its ratio, its upper edge at [top]. Returns the drawn height. */
fun PDPageContentStream.image(image: PDImageXObject, x: Float, top: Float, width: Float): Float {
    val height = width * image.height / image.width
    drawImage(image, x, top - height, width, height)
    return height
}

/** A filled rectangle whose lower left corner is ([x], [y]). */
fun PDPageContentStream.rect(x: Float, y: Float, width: Float, height: Float, color: Color) {
    setNonStrokingColor(color)
    addRect(x, y, width, height)
    fill()
}

/** The outline of a rectangle whose lower left corner is ([x], [y]). */
fun PDPageContentStream.strokedRect(x: Float, y: Float, width: Float, height: Float, color: Color, lineWidth: Float = 1f) {
    setStrokingColor(color)
    setLineWidth(lineWidth)
    addRect(x, y, width, height)
    stroke()
}

fun PDPageContentStream.horizontalLine(x: Float, y: Float, width: Float, color: Color, lineWidth: Float) {
    setStrokingColor(color)
    setLineWidth(lineWidth)
    moveTo(x, y)
    lineTo(x + width, y)
    stroke()
}

/** Adds a circle to the current path, to be filled or stroked. */
fun PDPageContentStream.circle(centerX: Float, centerY: Float, radius: Float) {
    val k = radius * KAPPA
    moveTo(centerX + radius, centerY)
    curveTo(centerX + radius, centerY + k, centerX + k, centerY + radius, centerX, centerY + radius)
    curveTo(centerX - k, centerY + radius, centerX - radius, centerY + k, centerX - radius, centerY)
    curveTo(centerX - radius, centerY - k, centerX - k, centerY - radius, centerX, centerY - radius)
    curveTo(centerX + k, centerY - radius, centerX + radius, centerY - k, centerX + radius, centerY)
    closePath()
}

/** Adds a rectangle with rounded corners to the current path, to be filled or stroked. */
fun PDPageContentStream.roundedRect(x: Float, y: Float, width: Float, height: Float, radius: Float) {
    val k = radius * KAPPA
    moveTo(x + radius, y)
    lineTo(x + width - radius, y)
    curveTo(x + width - radius + k, y, x + width, y + radius - k, x + width, y + radius)
    lineTo(x + width, y + height - radius)
    curveTo(x + width, y + height - radius + k, x + width - radius + k, y + height, x + width - radius, y + height)
    lineTo(x + radius, y + height)
    curveTo(x + radius - k, y + height, x, y + height - radius + k, x, y + height - radius)
    lineTo(x, y + radius)
    curveTo(x, y + radius - k, x + radius - k, y, x + radius, y)
    closePath()
}

// bezier approximation of a quarter circle
private const val KAPPA = 0.5523f
