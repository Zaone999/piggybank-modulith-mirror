package be.kdg.piggybankmodulith.grandparents.adapter.in.rest;

import be.kdg.piggybankmodulith.grandparents.domain.Grandchild;

import java.util.UUID;

public record GrandchildResponse(UUID grandchildId, String firstName, String lastName) {

    public static GrandchildResponse from(Grandchild grandchild) {
        return new GrandchildResponse(grandchild.id().uuid(), grandchild.firstName(), grandchild.lastName());
    }
}
