package status200.com.br.celhas.dao;

/**
 * Created by Paulo on 28/03/2015.
 */
public class ScriptSQL {

     public static String getCreateContato() {

          StringBuilder sqlBuilder = new StringBuilder();
          sqlBuilder.append(" CREATE TABLE IF NOT EXISTS CLIENTE ( ");
          sqlBuilder.append("_id                INTEGER       NOT NULL ");
          sqlBuilder.append("PRIMARY KEY AUTOINCREMENT, ");
          sqlBuilder.append("NOME               VARCHAR (200), ");
          sqlBuilder.append("TELEFONE           VARCHAR (14) ");
          sqlBuilder.append(");");

          return sqlBuilder.toString();

     }
}
