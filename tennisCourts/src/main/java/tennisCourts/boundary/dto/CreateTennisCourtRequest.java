package tennisCourts.boundary.dto;

import tennisCourts.entity.Surface;

public record CreateTennisCourtRequest(String name, String country, String city, Surface surface) {
}
