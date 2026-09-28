package be.kdg.piggybankmodulith.grandparents.adapter.in.rest;

import be.kdg.piggybankmodulith.grandparents.port.in.GrandchildQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/grandchildren")
public class GrandchildController {

    private final GrandchildQuery grandchildQuery;

    public GrandchildController(GrandchildQuery grandchildQuery) {
        this.grandchildQuery = grandchildQuery;
    }

    @GetMapping
    public List<GrandchildResponse> listGrandchildren() {
        return grandchildQuery.listGrandchildren().stream()
                .map(GrandchildResponse::from)
                .toList();
    }
}
