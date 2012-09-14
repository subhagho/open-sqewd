/**
 * Copyright 2012 Subho Ghosh (subho.ghosh at outlook dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @filename LocalResultSet.java
 * @created Sep 8, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class LocalResultSet implements ResultSet {
	private boolean isOpen = false;

	private final HashMap<String, StructDbColumn> columns = new HashMap<String, StructDbColumn>();
	private int current = -1;
	private ArrayList<LocalResult> results = null;
	private Object lastObject = null;
	private String cursorname = null;
	private int fetchDirection = 1;
	private HashMap<String, String> types = new HashMap<String, String>();

	public LocalResultSet() {

	}

	public void create(final String type, final ResultSet rs) throws Exception {
		columns.clear();
		current = -1;
		lastObject = null;
		cursorname = null;
		types.clear();

		types.put(type, type);

		ResultSetMetaData md = rs.getMetaData();
		if (md != null) {
			for (int ii = 1; ii <= md.getColumnCount(); ii++) {
				StructDbColumn column = new StructDbColumn();
				column.Name = md.getColumnLabel(ii);
				column.Index = ii;
				column.Type = SQLDataType.type(md.getColumnType(ii));
				columns.put(column.Name, column);
			}
			results = new ArrayList<LocalResult>();
			while (rs.next()) {
				LocalResult lrs = new LocalResult(type, columns.size());
				for (int ii = 1; ii <= md.getColumnCount(); ii++) {
					String column = md.getColumnLabel(ii);
					Object value = rs.getObject(column);
					if (!rs.wasNull()) {
						lrs.add(ii, value);
					} else {
						lrs.add(ii, null);
					}
				}
				results.add(lrs);
			}
			isOpen = true;
		} else
			throw new SQLException("Invalid ResultSet, Meta-data is null.");
	}

	public void append(final String type, final LocalResultSet rs)
			throws Exception {
		if (!types.containsKey(type)) {
			int index = columns.size();
			for (String column : rs.columns.keySet()) {
				StructDbColumn ocol = rs.columns.get(column);
				StructDbColumn ncol = new StructDbColumn();
				ncol.Name = ocol.Name;
				ncol.Type = ocol.Type;
				ncol.Index = index;
				columns.put(column, ncol);
				index++;
			}
			types.put(type, type);
		}
		LocalResult lrtgt = rs.results.get(rs.current);
		LocalResult lrsrc = results.get(current);
		if (!lrsrc.hasColumns(type)) {
			lrsrc.initarray(type, columns.size());
		} else if (lrsrc.hasData(type)) {
			lrsrc = copyCurrent();
		}
		lrsrc.dataSet(type);

		for (String column : rs.columns.keySet()) {
			int index = rs.columns.get(column).Index;
			Object value = lrtgt.get(index);
			if (value != null) {
				lrsrc.add(index, value);
			}
		}
	}

	public StructDbColumn getColumn(final String name) {
		if (columns.containsKey(name))
			return columns.get(name);
		return null;
	}

	public LocalResult copyCurrent() throws SQLException {
		isResultSetValid();
		if (current < 0 || current > results.size())
			throw new SQLException("Invalid Current index. Index [" + current
					+ "]");
		LocalResult rs = results.get(current);
		if (rs == null)
			throw new SQLException(
					"Invalid data at current location. Result object is null.");
		LocalResult cp = rs.copy();
		results.add(current + 1, cp);
		current++;

		return cp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(final Class<?> arg0) throws SQLException {
		return false;
	}

	private boolean isResultSetValid() throws SQLException {
		if (!isOpen)
			throw new SQLException("ResultSet has been closed.");

		if (results != null) {
			if (current < 0)
				throw new SQLException("ResultSet iterator not initialzied.");
			if (current >= results.size())
				throw new SQLException(
						"ResultSet exhausted. Last record has been consumed.");
		} else
			throw new SQLException("ResultSet not open or result set is empty.");
		return true;
	}

	private void throwConvertException(final Class<?> src, final Class<?> tgt)
			throws SQLException {
		throw new SQLException("Cannot convert [" + src.getCanonicalName()
				+ "] to [" + tgt.getCanonicalName() + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public <T> T unwrap(final Class<T> arg0) throws SQLException {
		throw new SQLException("Method uniplemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#absolute(int)
	 */
	public boolean absolute(final int arg0) throws SQLException {
		isResultSetValid();
		if (arg0 > 0 && arg0 < results.size()) {
			current = arg0;
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#afterLast()
	 */
	public void afterLast() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#beforeFirst()
	 */
	public void beforeFirst() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#close()
	 */
	public void close() throws SQLException {
		isOpen = false;
		results.clear();
		results = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#deleteRow()
	 */
	public void deleteRow() throws SQLException {
		isResultSetValid();

		if (current >= 0 && current < results.size()) {
			results.remove(current);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#findColumn(java.lang.String)
	 */
	public int findColumn(final String arg0) throws SQLException {
		if (columns.containsKey(arg0))
			return columns.get(arg0).Index;
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#first()
	 */
	public boolean first() throws SQLException {
		if (results != null && results.size() > 0) {
			current = 0;
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getArray(int)
	 */
	public Array getArray(final int arg0) throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	public Array getArray(final String arg0) throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	public InputStream getAsciiStream(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);

		lastObject = lr.get(arg0);
		if (lastObject != null) {
			String value = null;
			if (lastObject instanceof String) {
				value = (String) lastObject;
			} else {
				value = lastObject.toString();
			}
			return new ByteArrayInputStream(value.getBytes());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	public InputStream getAsciiStream(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getAsciiStream(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(final int arg0) throws SQLException {
		isResultSetValid();

		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof String) {
				try {
					DecimalFormat df = new DecimalFormat();
					df.setParseBigDecimal(true);
					return (BigDecimal) df.parse((String) lastObject);
				} catch (Exception ex) {
					throw new SQLException(ex);
				}
			} else if (lastObject instanceof BigDecimal)
				return (BigDecimal) lastObject;
			else {
				throwConvertException(lastObject.getClass(), BigDecimal.class);
			}

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getBigDecimal(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(int, int)
	 */
	public BigDecimal getBigDecimal(final int arg0, final int arg1)
			throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
	 */
	public BigDecimal getBigDecimal(final String arg0, final int arg1)
			throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	public InputStream getBinaryStream(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			byte[] bytes = null;
			if (lastObject instanceof byte[]) {
				bytes = (byte[]) lastObject;
			} else {
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutput out = null;
					out = new ObjectOutputStream(bos);
					out.writeObject(lastObject);
					bytes = bos.toByteArray();
				} catch (Exception ex) {
					throw new SQLException(ex);
				}
			}
			if (bytes != null)
				return new ByteArrayInputStream(bytes);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	public InputStream getBinaryStream(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getBinaryStream(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	public Blob getBlob(final int arg0) throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	public Blob getBlob(final String arg0) throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	public boolean getBoolean(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof Boolean)
				return (Boolean) lastObject;
			else if (lastObject instanceof String)
				return Boolean.parseBoolean((String) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Boolean.class);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getBoolean(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getByte(int)
	 */
	public byte getByte(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof Byte)
				return (Byte) lastObject;
			else {
				throwConvertException(lastObject.getClass(), Byte.class);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	public byte getByte(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getByte(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	public byte[] getBytes(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof byte[])
				return (byte[]) lastObject;
			else {
				throwConvertException(lastObject.getClass(), byte[].class);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	public byte[] getBytes(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getBytes(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	public Reader getCharacterStream(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			String value = null;
			if (lastObject instanceof String) {
				value = (String) lastObject;
			} else {
				value = lastObject.toString();
			}
			return new StringReader(value);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	public Reader getCharacterStream(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getCharacterStream(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getClob(int)
	 */
	public Clob getClob(final int arg0) throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	public Clob getClob(final String arg0) throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getCursorName()
	 */
	public String getCursorName() throws SQLException {
		return cursorname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(int)
	 */
	public Date getDate(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof java.util.Date) {
				java.util.Date dt = (java.util.Date) lastObject;
				return new Date(dt.getTime());
			} else if (lastObject instanceof Date)
				return (Date) lastObject;
			else if (lastObject instanceof Long)
				return new Date((Long) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Date.class);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	public Date getDate(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getDate(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	public Date getDate(final int arg0, final Calendar arg1)
			throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	public Date getDate(final String arg0, final Calendar arg1)
			throws SQLException {
		throw new SQLException("Not implemented...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	public double getDouble(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof Double)
				return (Double) lastObject;
			else if (lastObject instanceof Long)
				return (Double) lastObject;
			else if (lastObject instanceof Float)
				return (Double) lastObject;
			else if (lastObject instanceof Integer)
				return (Double) lastObject;
			else if (lastObject instanceof Short)
				return (Double) lastObject;
			else if (lastObject instanceof String)
				return Double.parseDouble((String) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Double.class);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getDouble(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return fetchDirection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	public float getFloat(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof Long)
				return (Float) lastObject;
			else if (lastObject instanceof Float)
				return (Float) lastObject;
			else if (lastObject instanceof Integer)
				return (Float) lastObject;
			else if (lastObject instanceof Short)
				return (Float) lastObject;
			else if (lastObject instanceof String)
				return Float.parseFloat((String) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Float.class);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	public float getFloat(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getFloat(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getInt(int)
	 */
	public int getInt(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof Integer)
				return (Integer) lastObject;
			else if (lastObject instanceof Short)
				return (Integer) lastObject;
			else if (lastObject instanceof String)
				return Integer.parseInt((String) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Integer.class);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	public int getInt(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getInt(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getLong(int)
	 */
	public long getLong(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof Long)
				return (Long) lastObject;
			else if (lastObject instanceof Integer)
				return (Long) lastObject;
			else if (lastObject instanceof Short)
				return (Long) lastObject;
			else if (lastObject instanceof String)
				return Long.parseLong((String) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Long.class);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	public long getLong(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getLong(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNCharacterStream(int)
	 */
	public Reader getNCharacterStream(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNCharacterStream(java.lang.String)
	 */
	public Reader getNCharacterStream(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNClob(int)
	 */
	public NClob getNClob(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNClob(java.lang.String)
	 */
	public NClob getNClob(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNString(int)
	 */
	public String getNString(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNString(java.lang.String)
	 */
	public String getNString(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(int)
	 */
	public Object getObject(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null)
			return lastObject;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	public Object getObject(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getObject(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	public Object getObject(final int arg0, final Map<String, Class<?>> arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject(final String arg0, final Map<String, Class<?>> arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRef(int)
	 */
	public Ref getRef(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	public Ref getRef(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRow()
	 */
	public int getRow() throws SQLException {
		isResultSetValid();
		return current;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRowId(int)
	 */
	public RowId getRowId(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRowId(java.lang.String)
	 */
	public RowId getRowId(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getSQLXML(int)
	 */
	public SQLXML getSQLXML(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getSQLXML(java.lang.String)
	 */
	public SQLXML getSQLXML(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getShort(int)
	 */
	public short getShort(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof Short)
				return (Short) lastObject;
			else if (lastObject instanceof String)
				return Short.parseShort((String) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Short.class);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	public short getShort(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getShort(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getStatement()
	 */
	public Statement getStatement() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getString(int)
	 */
	public String getString(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof String)
				return (String) lastObject;
			else
				return lastObject.toString();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	public String getString(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getString(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(int)
	 */
	public Time getTime(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof java.util.Date) {
				java.util.Date dt = (java.util.Date) lastObject;
				return new Time(dt.getTime());
			} else if (lastObject instanceof Time)
				return (Time) lastObject;
			else if (lastObject instanceof Long)
				return new Time((Long) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Time.class);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	public Time getTime(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getTime(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	public Time getTime(final int arg0, final Calendar arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	public Time getTime(final String arg0, final Calendar arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(final int arg0) throws SQLException {
		isResultSetValid();
		LocalResult lr = results.get(current);
		lastObject = lr.get(arg0);
		if (lastObject != null) {
			if (lastObject instanceof java.util.Date) {
				java.util.Date dt = (java.util.Date) lastObject;
				return new Timestamp(dt.getTime());
			} else if (lastObject instanceof Timestamp)
				return (Timestamp) lastObject;
			else if (lastObject instanceof Long)
				return new Timestamp((Long) lastObject);
			else {
				throwConvertException(lastObject.getClass(), Timestamp.class);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(final String arg0) throws SQLException {
		if (columns.containsKey(arg0)) {
			int index = columns.get(arg0).Index;
			return getTimestamp(index);
		} else
			throw new SQLException("Cannot find column [" + arg0 + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(final int arg0, final Calendar arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String,
	 * java.util.Calendar)
	 */
	public Timestamp getTimestamp(final String arg0, final Calendar arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getType()
	 */
	public int getType() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getURL(int)
	 */
	public URL getURL(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	public URL getURL(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getUnicodeStream(int)
	 */
	public InputStream getUnicodeStream(final int arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
	 */
	public InputStream getUnicodeStream(final String arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#insertRow()
	 */
	public void insertRow() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isAfterLast()
	 */
	public boolean isAfterLast() throws SQLException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isBeforeFirst()
	 */
	public boolean isBeforeFirst() throws SQLException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		return !isOpen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isFirst()
	 */
	public boolean isFirst() throws SQLException {
		isResultSetValid();
		if (current == 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isLast()
	 */
	public boolean isLast() throws SQLException {
		isResultSetValid();
		if (current >= results.size())
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#last()
	 */
	public boolean last() throws SQLException {
		isResultSetValid();
		current = results.size() - 1;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#moveToCurrentRow()
	 */
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#next()
	 */
	public boolean next() throws SQLException {
		if (!isOpen)
			throw new SQLException("ResultSet has been closed.");
		if (fetchDirection < 0)
			return previous();
		if (results != null) {
			if (current >= (results.size() - 1))
				return false;
			if (current < 0) {
				current = 0;
			} else if (current >= 0) {
				current++;
			}

		} else
			throw new SQLException("ResultSet not open or result set is empty.");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#previous()
	 */
	public boolean previous() throws SQLException {
		if (!isOpen)
			throw new SQLException("ResultSet has been closed.");
		if (fetchDirection > 0)
			return next();
		if (results != null) {
			if (current == 0)
				return false;
			if (current < 0) {
				current = 0;
			} else {
				current--;
			}

		} else
			throw new SQLException("ResultSet not open or result set is empty.");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#refreshRow()
	 */
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#relative(int)
	 */
	public boolean relative(final int arg0) throws SQLException {
		if (!isOpen)
			throw new SQLException("ResultSet has been closed.");
		if (results != null) {
			if (current > results.size())
				return false;
			if (current < 0)
				throw new SQLException(
						"ResultSet not opened. Call next() to start iterating over the result set.");
			int index = current + arg0;
			if (index >= 0 && index < results.size()) {
				current = index;
				return true;
			}

		} else
			throw new SQLException("ResultSet not open or result set is empty.");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#rowInserted()
	 */
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	public void setFetchDirection(final int arg0) throws SQLException {
		fetchDirection = arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	public void setFetchSize(final int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	public void updateArray(final int arg0, final Array arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	public void updateArray(final String arg0, final Array arg1)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream)
	 */
	public void updateAsciiStream(final int arg0, final InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 * java.io.InputStream)
	 */
	public void updateAsciiStream(final String arg0, final InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	public void updateAsciiStream(final int arg0, final InputStream arg1,
			final int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 * java.io.InputStream, int)
	 */
	public void updateAsciiStream(final String arg0, final InputStream arg1,
			final int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, long)
	 */
	public void updateAsciiStream(final int arg0, final InputStream arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 * java.io.InputStream, long)
	 */
	public void updateAsciiStream(final String arg0, final InputStream arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	public void updateBigDecimal(final int arg0, final BigDecimal arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBigDecimal(java.lang.String,
	 * java.math.BigDecimal)
	 */
	public void updateBigDecimal(final String arg0, final BigDecimal arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream)
	 */
	public void updateBinaryStream(final int arg0, final InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 * java.io.InputStream)
	 */
	public void updateBinaryStream(final String arg0, final InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	public void updateBinaryStream(final int arg0, final InputStream arg1,
			final int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 * java.io.InputStream, int)
	 */
	public void updateBinaryStream(final String arg0, final InputStream arg1,
			final int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream,
	 * long)
	 */
	public void updateBinaryStream(final int arg0, final InputStream arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 * java.io.InputStream, long)
	 */
	public void updateBinaryStream(final String arg0, final InputStream arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	public void updateBlob(final int arg0, final Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	public void updateBlob(final String arg0, final Blob arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream)
	 */
	public void updateBlob(final int arg0, final InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream)
	 */
	public void updateBlob(final String arg0, final InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream, long)
	 */
	public void updateBlob(final int arg0, final InputStream arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream,
	 * long)
	 */
	public void updateBlob(final String arg0, final InputStream arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	public void updateBoolean(final int arg0, final boolean arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
	 */
	public void updateBoolean(final String arg0, final boolean arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	public void updateByte(final int arg0, final byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
	 */
	public void updateByte(final String arg0, final byte arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	public void updateBytes(final int arg0, final byte[] arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
	 */
	public void updateBytes(final String arg0, final byte[] arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader)
	 */
	public void updateCharacterStream(final int arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 * java.io.Reader)
	 */
	public void updateCharacterStream(final String arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	public void updateCharacterStream(final int arg0, final Reader arg1,
			final int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 * java.io.Reader, int)
	 */
	public void updateCharacterStream(final String arg0, final Reader arg1,
			final int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, long)
	 */
	public void updateCharacterStream(final int arg0, final Reader arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 * java.io.Reader, long)
	 */
	public void updateCharacterStream(final String arg0, final Reader arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	public void updateClob(final int arg0, final Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	public void updateClob(final String arg0, final Clob arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader)
	 */
	public void updateClob(final int arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader)
	 */
	public void updateClob(final String arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader, long)
	 */
	public void updateClob(final int arg0, final Reader arg1, final long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader,
	 * long)
	 */
	public void updateClob(final String arg0, final Reader arg1, final long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	public void updateDate(final int arg0, final Date arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
	 */
	public void updateDate(final String arg0, final Date arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	public void updateDouble(final int arg0, final double arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
	 */
	public void updateDouble(final String arg0, final double arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	public void updateFloat(final int arg0, final float arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
	 */
	public void updateFloat(final String arg0, final float arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	public void updateInt(final int arg0, final int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateInt(java.lang.String, int)
	 */
	public void updateInt(final String arg0, final int arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	public void updateLong(final int arg0, final long arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateLong(java.lang.String, long)
	 */
	public void updateLong(final String arg0, final long arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader)
	 */
	public void updateNCharacterStream(final int arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
	 * java.io.Reader)
	 */
	public void updateNCharacterStream(final String arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader, long)
	 */
	public void updateNCharacterStream(final int arg0, final Reader arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
	 * java.io.Reader, long)
	 */
	public void updateNCharacterStream(final String arg0, final Reader arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(int, java.sql.NClob)
	 */
	public void updateNClob(final int arg0, final NClob arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.sql.NClob)
	 */
	public void updateNClob(final String arg0, final NClob arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader)
	 */
	public void updateNClob(final int arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader)
	 */
	public void updateNClob(final String arg0, final Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader, long)
	 */
	public void updateNClob(final int arg0, final Reader arg1, final long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader,
	 * long)
	 */
	public void updateNClob(final String arg0, final Reader arg1,
			final long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNString(int, java.lang.String)
	 */
	public void updateNString(final int arg0, final String arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNString(java.lang.String, java.lang.String)
	 */
	public void updateNString(final String arg0, final String arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	public void updateNull(final int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNull(java.lang.String)
	 */
	public void updateNull(final String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	public void updateObject(final int arg0, final Object arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
	 */
	public void updateObject(final String arg0, final Object arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	public void updateObject(final int arg0, final Object arg1, final int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object,
	 * int)
	 */
	public void updateObject(final String arg0, final Object arg1,
			final int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	public void updateRef(final int arg0, final Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	public void updateRef(final String arg0, final Ref arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRow()
	 */
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRowId(int, java.sql.RowId)
	 */
	public void updateRowId(final int arg0, final RowId arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRowId(java.lang.String, java.sql.RowId)
	 */
	public void updateRowId(final String arg0, final RowId arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateSQLXML(int, java.sql.SQLXML)
	 */
	public void updateSQLXML(final int arg0, final SQLXML arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateSQLXML(java.lang.String, java.sql.SQLXML)
	 */
	public void updateSQLXML(final String arg0, final SQLXML arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	public void updateShort(final int arg0, final short arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateShort(java.lang.String, short)
	 */
	public void updateShort(final String arg0, final short arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	public void updateString(final int arg0, final String arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
	 */
	public void updateString(final String arg0, final String arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	public void updateTime(final int arg0, final Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
	 */
	public void updateTime(final String arg0, final Time arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	public void updateTimestamp(final int arg0, final Timestamp arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTimestamp(java.lang.String,
	 * java.sql.Timestamp)
	 */
	public void updateTimestamp(final String arg0, final Timestamp arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#wasNull()
	 */
	public boolean wasNull() throws SQLException {
		if (lastObject == null)
			return true;
		return false;
	}

}
