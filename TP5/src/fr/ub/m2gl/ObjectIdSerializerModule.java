package fr.ub.m2gl;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectIdSerializerModule extends SimpleModule {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectIdSerializerModule() {
        super("ObjectIdSerializerModule");
        this.addSerializer(ObjectId.class, new ObjectIdSerializer());
    }

    public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

        @Override
        public void serialize(ObjectId value, JsonGenerator jgen
                , SerializerProvider provider) 
                throws IOException, JsonProcessingException {
            jgen.writeString(value.toString());
        }
    }
}