package com.v1ok.modelmapper.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.spi.ValueReader;

@Slf4j
public class ResultSetReader implements ValueReader<ResultSet> {

  @Override
  public Object get(ResultSet source, String memberName) {

    Object object = null;
    try {
      object = source.getObject(memberName);
    } catch (SQLException e) {
      log.error("get object value from ResultSet is error!", e);
    }

    return object;
  }

  @Override
  public Member<ResultSet> getMember(ResultSet source, String memberName) {

    Object value = get(source, memberName);

    if (value == null) {
      return null;
    }

    return new Member<ResultSet>(value.getClass()) {
      @Override
      public Object get(ResultSet source, String memberName) {
        return value;
      }
    };
  }

  @Override
  public Collection<String> memberNames(ResultSet source) {

    try {
      ResultSetMetaData metaData = source.getMetaData();

      int columnCount = metaData.getColumnCount();

      List<String> memberNames = new ArrayList<>(columnCount);

      for (int i = 1; i <= columnCount; i++) {
        String columnLabel = metaData.getColumnLabel(i);
        memberNames.add(columnLabel);
      }

      return memberNames;

    } catch (SQLException e) {
      log.error("get ResultSetMetaData is error!", e);
    }

    return null;
  }

}
