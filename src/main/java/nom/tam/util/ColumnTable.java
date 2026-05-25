package nom.tam.util;

/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 2004 - 2024 nom-tam-fits
 * %%
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import nom.tam.util.type.ElementType;

/**
 * <p>
 * Table data that is stored (internally) in column major format. This class has been completely re-written by A. Kovacs
 * for 1.18. We keep the old API for compatibility, but make some practical additions to it.
 * </p>
 * Note that while column tables are fine to use for accessing data from FITS ASCII tables, they are generally not
 * suitable for user-access of binary table data, because the column tables contain data in the format that is used for
 * storing values in the regular FITS table in the file. That is:
 * <ul>
 * <li>logical values are represented by <code>byte</code> entries of 'T', 'F' or '0'.</li>
 * <li>String values are represented as ASCII arrays bytes.</li>
 * <li>Complex values are represented by <code>float[2]</code> or <code>double[2]</code>.</li>
 * <li>Variable length columns of all types are represented by heap pointers of <code>int[2]</code> or
 * <code>long[2]</code>.</li>
 * </ul>
 *
 * @param <T> dummy generic type parameter that is no longer used. We'll stick to it a a memento of the bad design
 *                decisions of the past...
 */
public class ColumnTable<T> implements DataTable, Cloneable {

    /**
     * A list of columns contained in this table
     */
    private ArrayList<Column<?>> columns = new ArrayList<>();

    /**
     * The number of rows
     */
    private int nrow = 0;

    /**
     * The smallest dynamic allocation when addig / deleting rows
     */
    private static final int MIN_CAPACITY = 16;

    /**
     * Allow the client to provide opaque data.
     */
    private T extraState;

    /**
     * Creates an empty column table.
     *
     * @since 1.18
     */
    public ColumnTable() {
    }

    /**
     * Create the object after checking consistency.
     *
     * @param  arrays         An array of one-d primitive arrays.
     * @param  sizes          The number of elements in each row for the corresponding column
     *
     * @throws TableException if the structure of the columns is not consistent
     */
    public ColumnTable(Object[] arrays, int[] sizes) throws TableException {
        for (int i = 0; i < arrays.length; i++) {
            addColumn(arrays[i], sizes[i]);
        }
    }

    /**
     * Checks if the table is empty (contains no data and no column definitions)
     *
     * @return <code>true</code> if the table has no columns defined, otherwise <code>false</code>
     *
     * @since  1.18
     *
     * @see    #clear()
     * @see    #deleteAllRows()
     */
    public final boolean isEmpty() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Clears the table, discarding all columns.
     *
     * @see   #deleteAllRows()
     *
     * @since 1.18
     */
    public void clear() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Makes sure that the table is expanded to hold up to the specified number of rows without having to grow
     * dynamically. Typically it not necessary when adding new rows to the table, as the automatic dynamic allocation is
     * quite efficient, but if you know in advance how many rows you want to add to the table, it does not hurt to just
     * grow the table once, raher than a few times potentially. Note, that if deleting rows may annul the effect of this
     * call, and shrink the table to a reasonable size after the deletions.
     *
     * @param rows the number of rows we will want the table to hold at some point in the future...
     *
     * @see        #addRow(Object[])
     */
    public void ensureSize(int rows) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Checks a column data, in which elements have been already wrapped to ensure that the column is self consistent,
     * containing
     *
     * @param  newColumn      An array holding the column data with each entry corresponding to hte data for a row.
     *
     * @return                the element count
     *
     * @throws TableException if the data is inconsistent, or contains null, or non-arrays
     */
    private int checkWrappedColumn(Object newColumn) throws TableException, NullPointerException {
        // For array elements, check consistency...
        if (!(newColumn instanceof Object[])) {
            // Check as scalar column...
            checkFlatColumn(newColumn, 1);
            return 1;
        }
        int[] dims = null;
        try {
            dims = ArrayFuncs.checkRegularArray(newColumn, false);
        } catch (Exception e) {
            throw new TableException(e);
        }
        if (dims.length != 2) {
            throw new TableException("Not a 2D array: " + newColumn.getClass());
        }
        Object[] entries = (Object[]) newColumn;
        if (entries.length == 0) {
            return 0;
        }
        Object first = entries[0];
        if (!first.getClass().getComponentType().isPrimitive()) {
            throw new TableException("Entries are not a primitive arrays: " + first.getClass());
        }
        if (getNRows() > 0 && dims[0] != getNRows()) {
            throw new TableException("Mismatched row count: got " + dims[0] + ", expected " + getNRows());
        }
        return Array.getLength(first);
    }

    /**
     * Adds a column as an array of scalars or regular 1D primitve array elements.
     *
     * @param  newColumn      the column to add, either as a 1D array of scalar primitives, or a regular 2D array of
     *                            primitives, in which each row contains the same type of 1D primitive array of the same
     *                            sizes.
     *
     * @throws TableException if the new column is not a 1D or 2D array of primitives, or it it does not match the
     *                            number of existing table rows or if the column contains an irregular 2D array.
     *
     * @since                 1.18
     *
     * @see                   #getWrappedColumn(int)
     * @see                   #setWrappedColumn(int, Object)
     */
    @SuppressWarnings("unchecked")
    public void addWrappedColumn(Object newColumn) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Adds a new column with the specified primitive base class and element count.
     *
     * @param  type           the primitive class of the elements in this colymn, such as <code>boolean.class</code>
     * @param  size           the number of primitive elements (use 1 to create scalar columns)
     *
     * @throws TableException if the type is not a primitive type or the size is invalid.
     *
     * @see                   #addColumn(Object, int)
     *
     * @since                 1.18
     */
    public void addColumn(Class<?> type, int size) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Converts a one-dimensional flat array of elements to a wrapped column, in which each top-level entry contains the
     * specified number of row elements
     *
     * @param  data           a one-domensional primitive array
     * @param  size           the number of column data elements per row
     *
     * @return                the wrapped column data, in which each tp-level entry contains data for a specific row.
     *
     * @throws TableException If the data could not be converted
     */
    private Object wrapColumn(Object data, int size) throws TableException {
        checkFlatColumn(data, size);
        // Fold the 1D array into 2D array of subarrays for storing
        Class<?> type = data.getClass().getComponentType();
        int len = size == 0 ? nrow : Array.getLength(data) / size;
        // The parent array
        Object[] array = (Object[]) Array.newInstance(data.getClass(), len);
        int offset = 0;
        for (int i = 0; i < len; i++, offset += size) {
            // subarrays...
            array[i] = Array.newInstance(type, size);
            System.arraycopy(data, offset, array[i], 0, size);
        }
        return array;
    }

    /**
     * Adds a column in flattened 1D format, specifying the size of array 'elements'.
     *
     * @param  newColumn      the column to add.
     * @param  size           size for the column
     *
     * @throws TableException if the new column is not a 1D array of primitives, or it it does not conform to the number
     *                            of existing table rows for the given element size.
     *
     * @see                   #addColumn(Class, int)
     * @see                   #setColumn(int, Object)
     */
    @SuppressWarnings("unchecked")
    public void addColumn(Object newColumn, int size) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the next standard table capacity step that will allow adding at least one more row to the table.
     *
     * @return the standard capacity (number of rows) to which we'd want to grow to contain another table row.
     */
    private int nextLargerCapacity() {
        return nextLargerCapacity(nrow);
    }

    /**
     * Returns the next standard table capacity step that will allow adding at least one more row to the table.
     *
     * @param  rows The number of rows we should be able to store.
     *
     * @return      the standard capacity (number of rows) to which we'd want to grow to contain another table row.
     */
    private int nextLargerCapacity(int rows) {
        if (rows < MIN_CAPACITY) {
            return MIN_CAPACITY;
        }
        // Predictable doubling beyond the minimum size...
        return (int) Math.min(Long.highestOneBit(rows) << 1, Integer.MAX_VALUE);
    }

    /**
     * Checks the integrity of an array containing element for a new row, with each element corresponding to an entry to
     * the respective table column.
     *
     * @param  row            An array containing data for a new table row.
     *
     * @throws TableException If the row structure is inconsistent with that of the table.
     */
    private void checkRow(Object[] row) throws TableException {
        // Check that the row matches existing columns
        if (row.length != columns.size()) {
            throw new TableException("Mismatched row size: " + row.length + ", expected " + columns.size());
        }
        for (int col = 0; col < row.length; col++) {
            Column<?> c = columns.get(col);
            c.checkEntry(row[col]);
        }
    }

    /**
     * Add a row to the table. Each element of the row must be a 1D primitive array. If this is not the first row in the
     * table, each element must match the types and sizes of existing rows.
     *
     * @param  row            the row to add
     *
     * @throws TableException if the row contains other than 1D primitive array elements or if the elements do not match
     *                            the types and sizes of existing table columns.
     *
     * @see                   #deleteRow(int)
     * @see                   #ensureSize(int)
     */
    public void addRow(Object[] row) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Checks if a data in column format is consistent with this table, for example before adding it.
     *
     * @param  data           A one-dimensional representation of the column data
     * @param  size           the number of primitive elements per table row
     *
     * @throws TableException if the column is not consistent with the current table structure.
     */
    private void checkFlatColumn(Object data, int size) throws TableException {
        if (!data.getClass().isArray()) {
            throw new TableException("Argument is not an array: " + data.getClass());
        }
        int len = Array.getLength(data);
        // Data cannot be null here (we check upstream)
        // if (data == null) {
        // throw new TableException("Unexpected null column");
        // }
        if (size > 0 && len % size != 0) {
            throw new TableException("The column size " + len + " is not a multiple of the element size " + size);
        }
        if (nrow == 0 || size == 0) {
            return;
        }
        if (len != nrow * size) {
            throw new TableException("Mismatched element count: " + len + ", expected " + (nrow * size) + " for " + nrow + " rows of size " + size);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ColumnTable<T> clone() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a deep copy of this column table, such that modification to either the original or the copy will not
     * affect the other.
     *
     * @return                A deep (independent) copy of this column table
     *
     * @throws TableException (<i>for back compatibility</i>) never thrown.
     */
    @SuppressWarnings("cast")
    public ColumnTable<T> copy() throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Deletes a column from this table.
     *
     * @param  col            the 0-based column index.
     *
     * @throws TableException if the column index is out of bounds
     *
     * @see                   #deleteColumns(int, int)
     *
     * @since                 1.18
     */
    public void deleteColumn(int col) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Delete a contiguous set of columns from the table.
     *
     * @param  start          The first column (0-indexed) to be deleted.
     * @param  len            The number of columns to be deleted.
     *
     * @throws TableException if the request goes outside the boundaries of the table or if the length is negative.
     *
     * @see                   #deleteColumn(int)
     */
    public void deleteColumns(int start, int len) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Delete all rows from the table, but leaving the column structure intact.
     *
     * @throws TableException if the request goes outside the boundaries of the table or if the length is negative.
     *
     * @see                   #deleteRows(int, int)
     * @see                   #clear()
     */
    public final void deleteAllRows() throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Delete a row from the table.
     *
     * @param  row            The row (0-indexed) to be deleted.
     *
     * @throws TableException if the request goes outside the boundaries of the table or if the length is negative.
     *
     * @see                   #deleteRows(int, int)
     */
    public final void deleteRow(int row) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Delete a contiguous set of rows from the table.
     *
     * @param  from           the 0-based index of the first tow to be deleted.
     * @param  length         The number of rows to be deleted.
     *
     * @throws TableException if the request goes outside the boundaries of the table or if the length is negative.
     *
     * @see                   #deleteRow(int)
     */
    public void deleteRows(int from, int length) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the primitive based class of the elements in a given colum.
     *
     * @param  col the 0-based column index
     *
     * @return     the primitive base class of the elements in the column, e.g. <code>short.class</code>.
     *
     * @since      1.18
     */
    public final Class<?> getElementClass(int col) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Get the base classes of the columns. As of 1.18, this method returns a copy ot the array used internally, which
     * is safe to modify.
     *
     * @return     An array of Class objects, one for each column.
     *
     * @deprecated Use {@link #getElementClass(int)} instead. This method may be removed in the future.
     *
     * @see        #getElementClass(int)
     */
    @Deprecated
    public Class<?>[] getBases() {
        Class<?>[] bases = new Class<?>[columns.size()];
        for (int i = 0; i < bases.length; i++) {
            bases[i] = getElementClass(i);
        }
        return bases;
    }

    /**
     * Returns the wrapped column data, in which each entry correspond to data for a given row. If the column contains
     * non-scalar elements, then each entry in the returned array will be a primitive array of the column's element
     * size.
     *
     * @param  col the 0-based column index
     *
     * @return     the array in which each entry corresponds to table row. For scalar columns this will be a primitive
     *                 array of {@link #getNRows()} entries. Otherwise, it will be an array, whch contains the primitive
     *                 array elements for each row.
     *
     * @see        #getColumn(int)
     * @see        #addWrappedColumn(Object)
     * @see        #setWrappedColumn(int, Object)
     *
     * @since      1.18
     */
    public Object getWrappedColumn(int col) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * <p>
     * Returns the data for a particular column in as a single 1D array of primitives. See
     * {@link nom.tam.fits.TableData#addColumn(Object)} for more information about the format of data elements in
     * general.
     * </p>
     *
     * @param  col The 0-based column index.
     *
     * @return     an array of primitives (for scalar columns), or else an <code>Object[]</code> array, or possibly
     *                 <code>null</code>
     *
     * @see        #getWrappedColumn(int)
     * @see        #setColumn(int, Object)
     * @see        #getElement(int, int)
     * @see        #getNCols()
     */
    @Override
    public Object getColumn(int col) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the data for all columns as an array of flattened 1D primitive arrays.
     *
     * @return An array containing the flattened data for each column. Each columns's data is represented by a single 1D
     *             array holding all elements for that column. Because this is not an easily digestible format, you are
     *             probably better off using {@link #getElement(int, int)} or {@link #getRow(int)} instead.
     *
     * @see    #getColumn(int)
     */
    public Object[] getColumns() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Object getElement(int row, int col) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the extra state information of the table. The type and nature of this information is implementation
     * dependent.
     *
     * @return     the object capturing the implementation-specific table state
     *
     * @deprecated (<i>for internal use</i>) No longer used, will be removed in the future.
     */
    @Deprecated
    public T getExtraState() {
        return extraState;
    }

    @Override
    public final int getNCols() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public final int getNRows() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Object getRow(int row) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the size of 1D array elements stored in a given column
     *
     * @param  col the 0-based column index
     *
     * @return     the array size in the column (scalars return 1)
     *
     * @since      1.18
     */
    public final int getElementSize(int col) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the flattened (1D) size of elements in each column of this table. As of 1.18, this method returns a copy
     * ot the array used internally, which is safe to modify.
     *
     * @return     an array with the byte sizes of each column
     *
     * @see        #getElementSize(int)
     *
     * @deprecated Use {@link #getElementSize(int)} instead. This method may be removed in the future.
     *
     * @since      1.18
     */
    @Deprecated
    public int[] getSizes() {
        int[] sizes = new int[columns.size()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = getElementSize(i);
        }
        return sizes;
    }

    /**
     * Returns the Java array type character for the elements stored in a given column.
     *
     * @param  col the 0-based column index
     *
     * @return     the Java array type of the elements in the column, such as 'I' if the array is class is
     *                 <code>I[</code> (that is for <code>int[]</code>).
     */
    public final char getTypeChar(int col) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Get the characters describing the base classes of the columns. As of 1.18, this method returns a copy ot the
     * array used internally, which is safe to modify.
     *
     * @return     An array of type characters (Java array types), one for each column.
     *
     * @deprecated Use {@link #getTypeChar(int)} instead. This method may be removed in the future.
     *
     * @see        #getTypeChar(int)
     */
    @Deprecated
    public char[] getTypes() {
        char[] types = new char[columns.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = getTypeChar(i);
        }
        return types;
    }

    /**
     * Reads the table's data from the input, in row-major format
     *
     * @param  in           The input to read from.
     *
     * @throws EOFException is already at the end of file.
     * @throws IOException  if the reading failed
     *
     * @see                 #write(ArrayDataOutput)
     */
    public void read(ArrayDataInput in) throws EOFException, IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setColumn(int col, Object newColumn) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Sets new data for a column, in wrapped format. The argument is an 1D or 2D array of primitives, in which the
     * eading dimension must match the number of rows already in the table (if any).
     *
     * @param  col            the zero-based column index
     * @param  newColumn      the new column data, either as a 1D array of scalar primitives, or a regular 2D array of
     *                            primitives, in which each row contains the same type of 1D primitive array of the same
     *                            sizes.
     *
     * @throws TableException if the new column data is not a 1D or 2D array of primitives, or it it does not match the
     *                            number of existing table rows or if the column contains an irregular 2D array.
     *
     * @since                 1.18
     *
     * @see                   #getWrappedColumn(int)
     * @see                   #addWrappedColumn(Object)
     */
    @SuppressWarnings("unchecked")
    public void setWrappedColumn(int col, Object newColumn) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void setElement(int row, int col, Object x) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Store additional information that may be needed by the client to regenerate initial arrays.
     *
     * @param      opaque the extra state to set.
     *
     * @deprecated        (<i>for internal use</i>) No longer used, will be removed in the future. We used the extra
     *                        state to carry properties of an enclosing class in this enclosed object, so we could
     *                        inherit those to new enclosing class instances. This is bad practie. If one needs data
     *                        from the enclosing object, it should be handled by passing the enclsing object, and not
     *                        this enclosed table.
     */
    @Deprecated
    public void setExtraState(T opaque) {
        extraState = opaque;
    }

    @Override
    public void setRow(int row, Object data) throws TableException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Writes the table's data to an output, in row-major format.
     *
     * @param  out         the output stream to write to.
     *
     * @throws IOException if the write operation failed
     *
     * @see                #read(ArrayDataInput)
     */
    public void write(ArrayDataOutput out) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Write a column of a table.
     *
     * @param  out         the output stream to write to.
     * @param  rowStart    first row to write
     * @param  rowEnd      the exclusive ending row index (not witten)
     * @param  col         the zero-based column index to write.
     *
     * @throws IOException if the write operation failed
     */
    public void write(ArrayDataOutput out, int rowStart, int rowEnd, int col) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Reads a column of a table from a
     *
     * @param  in           The input stream to read from.
     * @param  rowStart     first row to read
     * @param  rowEnd       the exclusive ending row index (not read)
     * @param  col          the zero-based column index to read.
     *
     * @throws EOFException is already at the end of file.
     * @throws IOException  if the reading failed
     */
    public void read(ArrayDataInput in, int rowStart, int rowEnd, int col) throws EOFException, IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private Column<?> createColumn(Class<?> type, int size) throws TableException {
        if (type == null) {
            throw new TableException("Column type cannot be null.");
        }
        if (!type.isPrimitive()) {
            throw new TableException("Not a primitive base type: " + type.getName());
        }
        if (size == 1) {
            if (type.equals(byte.class)) {
                return new Bytes();
            }
            if (type.equals(boolean.class)) {
                return new Booleans();
            }
            if (type.equals(char.class)) {
                return new Chars();
            }
            if (type.equals(short.class)) {
                return new Shorts();
            }
            if (type.equals(int.class)) {
                return new Integers();
            }
            if (type.equals(long.class)) {
                return new Longs();
            }
            if (type.equals(float.class)) {
                return new Floats();
            }
            if (type.equals(double.class)) {
                return new Doubles();
            }
        }
        return new Generic(type, size);
    }

    /**
     * Container for one column in the table.
     *
     * @author        Attila Kovacs
     *
     * @param  <Data> The generic type of the data elements held in the table
     */
    private abstract static class Column<Data> implements Cloneable {

        protected Data data;

        private ElementType<?> fitsType;

        /**
         * Instantiates a new column data container
         *
         * @param fitsType The primitive element type that is used to store data for this column in the FITS
         */
        Column(ElementType<?> fitsType) {
            this.fitsType = fitsType;
            init(fitsType.primitiveClass());
        }

        @SuppressWarnings("unchecked")
        void init(Class<?> storeType) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the data array that holds all elements (one entry per row) for this column. It may be a primitive
         * array or an object array.
         *
         * @return the array that holds data for this column.
         */
        Data getData() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the data a a 1D array containing all elements for this column. It may be a primitive array or an
         * object array.
         *
         * @return the array that holds data for this column.
         */
        Object getFlatData() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the FITS element type contained in this column.
         *
         * @return the FITS type of data elements as they are stored in FITS
         */
        ElementType<?> getElementType() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the primitive element type which reprensents data for this column in FITS.
         *
         * @return the primitive type that is used when writing this column's data into FITS
         */
        Class<?> baseType() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the array data class that stores elements in this column.
         *
         * @return the class of array that holds elements in this column
         */
        Class<?> arrayType() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the number of basic elements stored per row in this column.
         *
         * @return the number of basic elements per row
         */
        int elementCount() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the number of rows currently allocated in this column, which may exceed the number of entries
         * currently populated.
         *
         * @return the number of rows allocated at present
         */
        int capacity() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        void deleteRows(int from, int len, int size, int maxCapacity) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Reads a single table entry from an input
         *
         * @param  index        the zero-based row index of the column entry
         * @param  in           the input to read from
         *
         * @return              the number of bytes read from the input.
         *
         * @throws EOFException if already at the end of file.
         * @throws IOException  if the entry could not be read
         */
        abstract int read(int index, ArrayDataInput in) throws EOFException, IOException;

        /**
         * Writes a since table entry to an output
         *
         * @param  index       the zero-based row index of the column entry
         * @param  out         the output to write to
         *
         * @throws IOException if the entry could not be written
         */
        abstract void write(int index, ArrayDataOutput out) throws IOException;

        /**
         * Reads a sequence of consecutive table entries from an input
         *
         * @param  from         the zero-based row index of the first column entry to read
         * @param  n            the number of consecutive rows to read
         * @param  in           the input to read from
         *
         * @return              the number of bytes read from the input.
         *
         * @throws EOFException if already at the end of file.
         * @throws IOException  if the entry could not be read
         */
        abstract int read(int from, int n, ArrayDataInput in) throws EOFException, IOException;

        /**
         * Writes a sequence of consecutive table entries to an output
         *
         * @param  from        the zero-based row index of the first column entry to write
         * @param  n           the number of consecutive rows to write
         * @param  out         the output to write to
         *
         * @throws IOException if the entry could not be written
         */
        abstract void write(int from, int n, ArrayDataOutput out) throws IOException;

        /**
         * Checks if an object is consistent with becoming an entry in this column.
         *
         * @param  x              an object that is expected to be a compatible column entry
         *
         * @throws TableException if the object is not consistent with the data expected for this column.
         */
        void checkEntry(Object x) throws TableException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Returns the entry at the specified index as an array. Primitive elements will be wrapped into an array of one
         *
         * @param  i the zero-based row index of the entry.
         *
         * @return   the entry as an array. Primitive values will be returned as a new array of one.
         */
        abstract Object getArrayElement(int i);

        /**
         * Sets a new array entry at the specified index. Primitive values must be be wrapped into an array of one.
         *
         * @param i the zero-based row index of the entry.
         * @param o the new entry as an array. Primitive values must be wrapped into an array of one.
         */
        abstract void setArrayElement(int i, Object o);

        /**
         * Resized this columns storage to an allocation of the specified size. The size should be greater or equals to
         * the number of elements already contained to avoid loss of data.
         *
         * @param size the new allocation (number of rows that can be contained)
         */
        abstract void resize(int size);

        /**
         * Ensures that the column has storage allocated to contain the speciifed number of entries, and grows the
         * column's allocation as necessaty to contain the specified number of rows in the future.
         *
         * @param size the number of rows that the table should be able to contain
         */
        void ensureSize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Trims the table to the specified size, as needed. The size should be greater or equals to the number of
         * elements already contained to avoid loss of data.
         *
         * @param size the new allocation (number of rows that can be contained)
         */
        void trim(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Column<Data> clone() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @SuppressWarnings("unchecked")
        Data copyData(int length) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        /**
         * Makes a copy of this column of the specified allocation for the number of rows in the copy. If the size is
         * less than the number of entries this column contains, the excess entries will be discared from the copy.
         *
         * @param  size the number of rows the copy might contain.
         *
         * @return      A copy of this column with storage for the specified number of rows.
         */
        Column<Data> copy(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of 8-bit bytes.
     *
     * @author Attila Kovacs
     */
    private static class Bytes extends Column<byte[]> {

        /**
         * Construct as new container for a byte-based data column
         */
        Bytes() {
            super(ElementType.BYTE);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        byte[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of boolean values.
     *
     * @author Attila Kovacs
     */
    private static class Booleans extends Column<boolean[]> {

        /**
         * Construct as new container for a boolean-based data column
         */
        Booleans() {
            super(ElementType.BOOLEAN);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        boolean[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of 16-bit unicode character values.
     *
     * @author Attila Kovacs
     */
    private static class Chars extends Column<char[]> {

        /**
         * Construct as new container for a unicode-based data column
         */
        Chars() {
            super(ElementType.CHAR);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        char[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of 16-bit integer values.
     *
     * @author Attila Kovacs
     */
    private static class Shorts extends Column<short[]> {

        /**
         * Construct as new container for a 16-bit integer based data column
         */
        Shorts() {
            super(ElementType.SHORT);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        short[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of 32-bit integer values.
     *
     * @author Attila Kovacs
     */
    private static class Integers extends Column<int[]> {

        /**
         * Construct as new container for a 32-bit integer based data column
         */
        Integers() {
            super(ElementType.INT);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of 64-bit integer values.
     *
     * @author Attila Kovacs
     */
    private static class Longs extends Column<long[]> {

        /**
         * Construct as new container for a 64-bit integer based data column
         */
        Longs() {
            super(ElementType.LONG);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        long[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of 32-bit floating point values.
     *
     * @author Attila Kovacs
     */
    private static class Floats extends Column<float[]> {

        /**
         * Construct as new container for a 32-bit floating-point based data column
         */
        Floats() {
            super(ElementType.FLOAT);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        float[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of 64-bit floating point values.
     *
     * @author Attila Kovacs
     */
    private static class Doubles extends Column<double[]> {

        /**
         * Construct as new container for a 32-bit floating-point based data column
         */
        Doubles() {
            super(ElementType.DOUBLE);
        }

        @Override
        void resize(int size) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        double[] getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * A column for data consisting of Objects (primitive arrays).
     *
     * @author Attila Kovacs
     */
    private static class Generic extends Column<Object[]> {

        private Class<?> type;

        private int size;

        /**
         * Construct as new container for an object (primitive array) based data column
         *
         * @param type the primitive type of elements this columns contains
         * @param size the primitive array size per column entry
         */
        Generic(Class<?> type, int size) {
            super(ElementType.forClass(type));
            this.type = type;
            this.size = size;
        }

        @Override
        void init(Class<?> storeType) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int elementCount() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void resize(int newSize) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int index, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int index, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        int read(int from, int n, ArrayDataInput in) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void write(int from, int n, ArrayDataOutput out) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        Object getArrayElement(int i) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        void setArrayElement(int i, Object o) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        Object[] copyData(int length) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        Object getFlatData() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }
}
