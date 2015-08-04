package it.playfellas.superapp.logic.db.query;

/**
 * Created by affo on 03/08/15.
 */
public abstract class QueryParam {
    private boolean not;

    public QueryParam() {
        this.not = false;
    }

    public QueryParam(boolean not) {
        this.not = not;
    }

    boolean isNot() {
        return not;
    }

    public abstract String getQuery();
}
