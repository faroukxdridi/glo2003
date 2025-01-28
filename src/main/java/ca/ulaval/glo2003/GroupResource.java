package ca.ulaval.glo2003;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.*;

@Path("/groups")
public class GroupResource {
    private static final Map<String, Group> groups = new HashMap<>(); // Stocke les groupes en mémoire

    // 🟢 /groups)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(Map<String, String> requestBody, @Context UriInfo uriInfo) {
        String groupName = requestBody.get("name");

        // les espaces
        if (groupName.contains(" ")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "error", "INVALID_PARAMETER",
                            "description", "Le nom du groupe contient un ou des espaces"
                    ))
                    .build();
        }

        // existe déjà
        if (groups.containsKey(groupName)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of(
                            "error", "CONFLICTING_PARAMETER",
                            "description", String.format("Le nom du groupe %s existe déjà", groupName)
                    ))
                    .build();
        }

        // ajoute à la liste
        Group newGroup = new Group(groupName);
        groups.put(groupName, newGroup);

        //URL
        URI location = uriInfo.getAbsolutePathBuilder().path(groupName).build();
        return Response.created(location).build(); // 201 CREATED avec le header Location
    }

    @GET
    @Path("/{name}") // Spécifie que {name} est un paramètre dans l'URL
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupByName(@PathParam("name") String name) {
        // Vérifie si un groupe avec ce nom existe
        if (!groups.containsKey(name)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "error", "ENTITY_NOT_FOUND",
                            "description", String.format("Le groupe %s n'existe pas", name)
                    ))
                    .build();
        }

        // Si le groupe existe, retourne ses informations (nom et membres vides)
        Map<String, Object> groupInfo = Map.of(
                "name", name,
                "members", new ArrayList<>() // Aucun membre pour le moment
        );

        return Response.ok(groupInfo).build();
    }


    // 🔵 Lister tous les groupes (GET /groups)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listGroups() {
        return Response.ok(groups.values()).build();
    }

    @DELETE
    @Path("/{name}") // Spécifie que {name} est un paramètre de chemin
    public Response deleteGroupByName(@PathParam("name") String name) {
        // Vérifie si le groupe existe
        if (!groups.containsKey(name)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "error", "ENTITY_NOT_FOUND",
                            "description", String.format("Le groupe %s n'existe pas", name)
                    ))
                    .build();
        }

        // Supprime le groupe de la Map
        groups.remove(name);

        // Retourne une réponse 204 No Content (succès sans corps)
        return Response.noContent().build();
    }

}
