package be.kdg.piggybankmodulith.grandparents.adapter.in.cli;

import be.kdg.piggybankmodulith.grandparents.port.in.GrandchildQuery;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GrandparentsShellCommands {

    private final GrandchildQuery grandchildQuery;

    public GrandparentsShellCommands(GrandchildQuery grandchildQuery) {
        this.grandchildQuery = grandchildQuery;
    }

    @Command(name = "grandchildren", description = "List the grandchildren the grandparents know")
    public String grandchildren() {
        return grandchildQuery.listGrandchildren().stream()
                .map(grandchild -> grandchild.fullName() + " (" + grandchild.id().uuid() + ")")
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
