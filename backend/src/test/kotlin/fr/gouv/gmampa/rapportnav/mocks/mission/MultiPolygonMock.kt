package fr.gouv.gmampa.rapportnav.mocks.mission

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon

object MultiPolygonMock {

    fun create(): MultiPolygon {
        val geometryFactory = GeometryFactory()

        val coords1 = arrayOf(
            Coordinate(30.0, 20.0),
            Coordinate(45.0, 40.0),
            Coordinate(10.0, 40.0),
            Coordinate(30.0, 20.0)
        )
        val polygon1 = geometryFactory.createPolygon(coords1)

        val coords2 = arrayOf(
            Coordinate(15.0, 5.0),
            Coordinate(40.0, 10.0),
            Coordinate(10.0, 20.0),
            Coordinate(5.0, 10.0),
            Coordinate(15.0, 5.0)
        )
        val polygon2 = geometryFactory.createPolygon(coords2)

        return geometryFactory.createMultiPolygon(arrayOf(polygon1, polygon2))
    }
}
