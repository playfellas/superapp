package it.playfellas.superapp.logic.db.query;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by affo on 03/08/15.
 */
public class Conjunction extends QueryParam {
    QueryParam[] params;

    public Conjunction(QueryParam... params) {
        this.params = params;
    }

    @Override
    public String getQuery() {
        String operator = isNot() ? "NOT" : "";
        String[] paramString = new String[params.length];
        for (int i = 0; i < paramString.length; i++) {
            paramString[i] = params[i].getQuery();
        }
        String where = StringUtils.join(paramString, " AND ");
        return operator + " ( " + where + " )";
    }
}
