package nyla.solutions.core.patterns.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindVariableInterpreter
{
    private final String sql;
    private final Map<String,Integer> fieldPositionMap;
    private final static String BIND_VAR_EXP = "(\\:+([a-zA-Z0-9]*[\\_]*[a-zA-Z0-9]*)*)";

    public BindVariableInterpreter(String sql)
    {

        StringBuilder preparedSql = new StringBuilder();
        fieldPositionMap = new HashMap<>();

        Pattern pattern = Pattern.compile(BIND_VAR_EXP);
        Matcher matcher = pattern.matcher(sql);

        int i =1;
        while(matcher.find()) {
            String variable = matcher.group();
            fieldPositionMap.put(formatStoredVariableName(variable),i++);

        }

        this.sql = matcher.replaceAll("?");
    }

    protected static String formatStoredVariableName(String varName)
    {
        return varName.trim().replace(":","")
                      .toUpperCase().replace(",","");
    }

    public Integer indexOf(String variable)
    {
        if(variable == null)
            throw new NullPointerException("Variable name not provided");

        Integer position = fieldPositionMap.get(variable.toUpperCase());
        if(position == null)
            throw new NullPointerException("Variable: "+variable+" not found in variables:"+fieldPositionMap.keySet());

        return position;
    }

    public String toPreparedStmtSql()
    {
        return sql;
    }


}
