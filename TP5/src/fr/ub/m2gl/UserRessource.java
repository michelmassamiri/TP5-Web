package fr.ub.m2gl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;

@Path("/users")
public class UserRessource {
	private MongoClient mongoclient;
	private MongoDatabase mongoDataBase;
	private MongoCollection<Document> collection;
	private ObjectMapper mapper;
	
	public UserRessource() {
		mongoclient = new MongoClient("localhost", 27017);
		mongoDataBase = mongoclient.getDatabase("TP5");
		collection = mongoDataBase.getCollection("User");
		mapper = new ObjectMapper();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response readUsers() {
		FindIterable<Document> iterDoc = collection.find();
		MongoCursor<Document> it = iterDoc.iterator();	
		List<Document> list = new ArrayList<Document>();
		
		while(it.hasNext()) {
			list.add(it.next());
		}
		
		return Response.ok(JSON.serialize(list)).build();
	}
		
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(final User user) {
		String result = "";
		if((collection.find(Filters.eq("id", user.getId()))
				.first()) != null ) {
			result = "User already exists !";
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}

		try {
			String jsonUser = mapper.writeValueAsString(user);
			Document document = Document.parse(jsonUser);			
			collection.insertOne(document);
			result = "User : "+ user.getFirstname() +" " 
					+ user.getLastname() +  " is saved\n";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(final User user) {
		String result = "";
		if((collection.find(Filters.eq("id", user.getId())).first()) == null) {
			result = "User was not found !";
			return Response.status(Response.Status.NOT_FOUND).entity(result).build();
		}
		
		try {
			String jsonUser = mapper.writeValueAsString(user);
			Document newUser = Document.parse(jsonUser);
			collection.replaceOne(Filters.eq("id", user.getId()), newUser);
			result = "User : "+ user.getFirstname() +" " 
					+ user.getLastname() +  " was successfully updated\n";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") long id) {
		String result = "";
		if(collection.find(Filters.eq("id", id)).first() == null) {
			result = "User not found !";
			return Response.status(Response.Status.NOT_FOUND).entity(result).build();
		}
		
		collection.deleteOne(Filters.eq("id", id));
		result = "User is deleted !";
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response readUser(@PathParam("id") long id) {
		String result = "";
		if(collection.find(Filters.eq("id", id)).first() == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(result).build();
		}
		
		Document userDocument = collection.find(Filters.eq("id", id)).first();
		result = userDocument.toJson();
		return Response.status(Response.Status.OK).entity(result).build();
	}
}
