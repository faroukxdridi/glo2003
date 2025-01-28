package ca.ulaval.glo2003;

import java.util.UUID;

public class Group {
    private final String id;
    private final String name;

    // Constructeur
    public Group(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    // Getter ID
    public String getId() {
        return id;
    }

    // Getter nom
    public String getName() {
        return name;
    }
}
