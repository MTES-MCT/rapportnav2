package fr.gouv.dgampa.rapportnav.config

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.locationtech.jts.io.geojson.GeoJsonWriter
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.core.JsonToken
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.deser.std.StdDeserializer
import tools.jackson.databind.ser.std.StdSerializer
import java.io.IOException

class JtsGeometryDeserializer(
    private val reader: GeoJsonReader = GeoJsonReader()
) : StdDeserializer<Geometry>(Geometry::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Geometry {
        val geoJson: String = when (p.currentToken()) {
            JsonToken.VALUE_STRING -> p.text
            else -> (p.readValueAsTree<JsonNode>()).toString()  // <-- no codec needed
        }

        try {
            return reader.read(geoJson)
        } catch (e: ParseException) {
            throw IOException("Invalid GeoJSON Geometry", e)
        }
    }
}

class JtsGeometrySerializer(
    private val writer: GeoJsonWriter = GeoJsonWriter()
) : StdSerializer<Geometry>(Geometry::class.java) {

    override fun serialize(
        value: Geometry?,
        gen: JsonGenerator?,
        provider: SerializationContext?
    ) {
        gen?.writeRawValue(writer.write(value))
    }
}

class JtsMultiPolygonDeserializer(
    private val reader: GeoJsonReader = GeoJsonReader(),
    private val gf: GeometryFactory = GeometryFactory()
) : StdDeserializer<MultiPolygon>(MultiPolygon::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MultiPolygon {
        val geoJson: String = when (p.currentToken()) {
            JsonToken.VALUE_STRING -> p.text
            else -> p.readValueAsTree<JsonNode>().toString()
        }

        val g: Geometry = try {
            reader.read(geoJson) // returns a JTS Geometry
        } catch (e: ParseException) {
            throw IOException("Invalid GeoJSON geometry", e)
        }

        return when (g) {
            is MultiPolygon -> g
            is Polygon -> gf.createMultiPolygon(arrayOf(g)) // promote Polygon -> MultiPolygon
            else -> throw IOException("Expected Polygon or MultiPolygon but got ${g.geometryType}")
        }
    }
}
