package it.playfellas.superapp.logic.db;

/**
 * Created by affo on 03/08/15.
 */
public class Conjunction extends QueryParam {
    QueryParam[] params;

    public Conjunction(QueryParam... params) {
        this.params = params;
    }

    @Override
    String getQuery() {
        return null;
    }
}
