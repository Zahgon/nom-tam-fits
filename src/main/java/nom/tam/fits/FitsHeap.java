package nom.tam.fits;

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
import java.io.IOException;
import nom.tam.util.ArrayDataInput;
import nom.tam.util.ArrayDataOutput;
import nom.tam.util.ByteArrayIO;
import nom.tam.util.FitsDecoder;
import nom.tam.util.FitsEncoder;

/**
 * Heap for storing variable-length entries in binary tables. FITS binary tables store variable length arrays on a heap,
 * following the regular array data. The newer implementation of the heap now provides proper random access to the byte
 * buffer as of version 1.16.
 */
public class FitsHeap implements FitsElement {

    /**
     * The minimum stoprage size to allocate for the heap, from which it can grow as necessary
     */
    private static final int MIN_HEAP_CAPACITY = 16384;

    // TODO
    // AK: In principle we could use ReadWriteAccess interface as the storage, which can be either an in-memory
    // array or a buffered file region. The latter could support heaps over 2G, and could reduce memory overhead
    // for heap access in some future release...
    /**
     * The underlying storage space of the heap
     */
    private ByteArrayIO store;

    /**
     * conversion from Java arrays to FITS binary representation
     */
    private FitsEncoder encoder;

    /**
     * conversion from FITS binary representation to Java arrays
     */
    private FitsDecoder decoder;

    /**
     * Construct a new uninitialized FITS heap object.
     */
    private FitsHeap() {
    }

    /**
     * Creates a heap of a given initial size. The new heap is initialized with 0's, and up to the specified number of
     * bytes are immediately available for reading (as zeroes). The heap can grow as needed if more data is written into
     * it.
     *
     * @throws IllegalArgumentException if the size argument is negative.
     */
    FitsHeap(int size) throws IllegalArgumentException {
        if (size < 0) {
            throw new IllegalArgumentException("Illegal size for FITS heap: " + size);
        }
        ByteArrayIO data = new ByteArrayIO(Math.max(size, MIN_HEAP_CAPACITY));
        data.setLength(Math.max(0, size));
        setData(data);
        encoder = new FitsEncoder(store);
        decoder = new FitsDecoder(store);
    }

    /**
     * Sets the underlying data storage for this heap instance. Constructors should call this.
     *
     * @param data the new underlying storage object for this heap instance.
     */
    protected synchronized void setData(ByteArrayIO data) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Add a copy constructor to allow us to duplicate a heap. This would be necessary if we wanted to copy an HDU that
     * included variable length columns.
     */
    synchronized FitsHeap copy() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Gets data for a Java array from the heap. The array may be a multi-dimensional array of arrays.
     *
     * @param  offset        the heap byte offset at which the data begins.
     * @param  array         The array of primitives to be extracted.
     *
     * @throws FitsException if the operation failed
     */
    public synchronized void getData(int offset, Object array) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public long getFileOffset() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public synchronized long getSize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Puts data to the end of the heap.
     *
     * @param  data a primitive array object, which may be multidimensional
     *
     * @return      the number of bytes used by the data.
     *
     * @see         #putData(Object, long)
     * @see         #getData(int, Object)
     */
    synchronized long putData(Object data) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Puts data onto the heap at a specific heap position.
     *
     * @param  data a primitive array object, which may be multidimensional
     * @param  pos  the byte offset at which the data should begin.
     *
     * @return      the number of bytes used by the data.
     *
     * @see         #putData(Object, long)
     * @see         #getData(int, Object)
     */
    synchronized long putData(Object data, long pos) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Copies a segment of data from another heap to the end of this heap
     *
     * @param  src    the heap to source data from
     * @param  offset the byte offset of the data in the source heap
     * @param  len    the number of bytes to copy
     *
     * @return        the position of the copied data in this heap.
     */
    synchronized int copyFrom(FitsHeap src, int offset, int len) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public synchronized void read(ArrayDataInput str) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public boolean reset() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void rewrite() throws IOException, FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public boolean rewriteable() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the current heap size.
     *
     * @return the size of the heap in bytes
     */
    public synchronized int size() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public synchronized void write(ArrayDataOutput str) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
