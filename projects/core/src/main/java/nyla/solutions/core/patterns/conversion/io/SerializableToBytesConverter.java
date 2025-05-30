package nyla.solutions.core.patterns.conversion.io;

import nyla.solutions.core.exception.NonSerializableException;
import nyla.solutions.core.patterns.conversion.Converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableToBytesConverter<ObjectType extends Serializable> implements Converter<ObjectType,byte[]> {
    private int byteSize = 32;

    @Override
    public byte[] convert(ObjectType sourceObject) {

        ByteArrayOutputStream out = new ByteArrayOutputStream(byteSize);
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(sourceObject);

            return out.toByteArray();
        } catch (IOException e) {
            throw new NonSerializableException(e);
        }
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }
}
