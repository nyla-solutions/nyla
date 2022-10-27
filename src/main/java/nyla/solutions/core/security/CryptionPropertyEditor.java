package nyla.solutions.core.security;

import nyla.solutions.core.util.Cryption;

import java.beans.PropertyEditorSupport;

/**
 * Helper class that can be uses by Spring to decryption properties
 * @author Gregory Green
 */
public class CryptionPropertyEditor extends PropertyEditorSupport {
    private final Cryption cryption;

    public CryptionPropertyEditor() {
        this(new Cryption());
    }
    public CryptionPropertyEditor(Cryption cryption) {
        this.cryption = cryption;
    }

    @Override
    public Object getValue() {
        Object value = super.getValue();
        if(value == null)
            return null;
        else if(String.class.isAssignableFrom(value.getClass()))
            return cryption.interpretText(String.valueOf(value));
        return value;
    }

    @Override
    public void setValue(Object value) {

        super.setValue(value);
    }
}
