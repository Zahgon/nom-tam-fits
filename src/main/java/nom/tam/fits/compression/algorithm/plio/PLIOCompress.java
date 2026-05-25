package nom.tam.fits.compression.algorithm.plio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import nom.tam.fits.compression.algorithm.api.ICompressor;

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
/**
 * (<i>for internal use</i>) The PLIO compression algorithm. The original decompression code was written by Doug Tody,
 * NRAO and included (ported to c and adapted) in cfitsio by William Pence, NASA/GSFC. That code was then ported to Java
 * by R. van Nieuwenhoven. Later it was massively refactored to harmonize the different compression algorithms and
 * reduce the duplicate code pieces without obscuring the algorithm itself as good as possible.
 *
 * @author Doug Tody
 * @author William Pence
 * @author Richard van Nieuwenhoven
 */
@SuppressWarnings("javadoc")
public abstract class PLIOCompress {

    public static class BytePLIOCompressor extends PLIOCompress implements ICompressor<ByteBuffer> {

        private ByteBuffer pixelData;

        @Override
        public boolean compress(ByteBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, ByteBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void put(int index, int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class ShortPLIOCompressor extends PLIOCompress implements ICompressor<ShortBuffer> {

        private ShortBuffer pixelData;

        @Override
        public boolean compress(ShortBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, ShortBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void put(int index, int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * Attention int values are limited to 24 bits!
     */
    public static class IntPLIOCompressor extends PLIOCompress implements ICompressor<IntBuffer> {

        private IntBuffer pixelData;

        @Override
        public boolean compress(IntBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, IntBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void put(int index, int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private static final int FIRST_VALUE_WITH_13_BIT = 4096;

    private static final int FIRST_VALUE_WITH_14_BIT = 8192;

    private static final int FIRST_VALUE_WITH_15_BIT = 16384;

    private static final int FIRST_VALUE_WITH_16_BIT = 32768;

    private static final int HEADER_SIZE_FIELD1 = 3;

    private static final int HEADER_SIZE_FIELD2 = 4;

    private static final int LAST_VALUE_FITTING_IN_12_BIT = FIRST_VALUE_WITH_13_BIT - 1;

    private static final int MINI_HEADER_SIZE = 3;

    private static final int MINI_HEADER_SIZE_FIELD = 2;

    /**
     * The exact meaning of this var is not clear at the moment of porting the algorithm to Java.
     */
    private static final int N20481 = 20481;

    private static final int OPCODE_1 = 1;

    private static final int OPCODE_2 = 2;

    private static final int OPCODE_3 = 3;

    private static final int OPCODE_4 = 4;

    private static final int OPCODE_5 = 5;

    private static final int OPCODE_6 = 6;

    private static final int OPCODE_7 = 7;

    private static final int OPCODE_8 = 8;

    private static final short[] PLIO_HEADER = { (short) 0, (short) 7, (short) -100, (short) 0, (short) 0, (short) 0, (short) 0 };

    private static final int SHIFT_12_BITS = 12;

    private static final int SHIFT_15_BITS = 15;

    private static final int VALUE_OF_BIT_13_AND14_ON = 12288;

    /**
     * PL_P2L -- Convert a pixel tiledImageOperation to a line list. The length of the list is returned as the function
     * value.
     *
     * @param compressedData encoded line list
     * @param npix           number of pixels to convert
     */
    protected void compress(ShortBuffer compressedData, int npix) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * PL_L2PI -- Translate a PLIO line list into an integer pixel tiledImageOperation. The number of pixels output
     * (always npix) is returned as the function value.
     *
     * @param  compressedData encoded line list
     * @param  npix           number of pixels to convert
     *
     * @return                number of pixels converted
     */
    protected int decompress(ShortBuffer compressedData, int npix) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    protected abstract int nextPixel();

    protected abstract void put(int index, int pixel);
}
