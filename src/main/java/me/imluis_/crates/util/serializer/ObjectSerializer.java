package me.imluis_.crates.util.serializer;

import lombok.Getter;

@Getter
public abstract class ObjectSerializer<T> {
	
	public abstract String serialize(T object);
	
	public abstract T deserialize(String source);
}
