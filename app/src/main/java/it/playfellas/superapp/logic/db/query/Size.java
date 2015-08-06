package it.playfellas.superapp.logic.db.query;

import it.playfellas.superapp.InternalConfig;

/**
 * Created by affo on 06/08/15.
 */
public class Size extends Atom {
    private int size;

    public Size(BinaryOperator op, int size) {
        super(op);
        this.size = size;
    }

    @Override
    protected String getColumnName() {
        return InternalConfig.KEY_SIZE;
    }

    @Override
    protected String getValue() {
        return Integer.toString(size);
    }
}
