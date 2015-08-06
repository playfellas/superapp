package it.playfellas.superapp.logic.db.query;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by affo on 06/08/15.
 */
public abstract class Operation extends Query {
    private Query[] params;
    private boolean not;

    public Operation(Query... params) {
        this.not = false;
        this.params = params;
    }

    public Operation(boolean not, Query... params) {
        this.not = not;
        this.params = params;
    }

    private String getNot() {
        return not ? Query.NOT : "";
    }

    @Override
    public String get() {
        String[] paramString = new String[params.length];
        for (int i = 0; i < paramString.length; i++) {
            paramString[i] = params[i].get();
        }
        String where = StringUtils.join(paramString, getOp().toString());
        return getNot() + " ( " + where + " )";
    }

    protected abstract BinaryOperator getOp();
}
