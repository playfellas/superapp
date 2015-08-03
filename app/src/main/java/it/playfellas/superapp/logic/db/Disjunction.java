package it.playfellas.superapp.logic.db;

/**
 * Created by affo on 03/08/15.
 */
public class Disjunction extends QueryParam {
    QueryParam[] params;

    public Disjunction(QueryParam... params) {
        this.params = params;
    }

    @Override
    String getQuery() {
        return null;
    }
}
