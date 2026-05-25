package nom.tam.image.compression.bintable;

/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 1996 - 2024 nom-tam-fits
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
import nom.tam.fits.header.Compression;

/**
 * (<i>for internal use</i>) The specifications of a binary table 'tile'.
 */
@SuppressWarnings("javadoc")
public final class BinaryTableTileDescription {

    private int rowStart;

    private int rowEnd;

    private int column;

    /**
     * 1-based tile index
     */
    private int tileIndex;

    private String compressionAlgorithm;

    public static BinaryTableTileDescription tile() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private BinaryTableTileDescription() {
        // use the static method to instantiate this class.
    }

    public BinaryTableTileDescription column(int value) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public BinaryTableTileDescription compressionAlgorithm(String value) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public BinaryTableTileDescription rowEnd(int value) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public BinaryTableTileDescription rowStart(int value) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Set the FITS table tile index
     *
     * @param  value The 1-based table tile index
     *
     * @return       itself
     */
    public BinaryTableTileDescription tileIndex(int value) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    protected int getColumn() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    protected String getCompressionAlgorithm() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    protected int getRowEnd() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    protected int getRowStart() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    protected int getTileIndex() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
