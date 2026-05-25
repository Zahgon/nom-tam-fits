package nom.tam.fits.compression.algorithm.gzip2;

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
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import nom.tam.fits.compression.algorithm.gzip.GZipCompressor;
import nom.tam.util.type.ElementType;

/**
 * (<i>for internal use</i>) The GZIP2 compression algorithm.
 *
 * @param <T> The genetic type of element buffer to compress
 */
@SuppressWarnings("javadoc")
public abstract class GZip2Compressor<T extends Buffer> extends GZipCompressor<T> {

    public static class ByteGZip2Compressor extends ByteGZipCompressor {
    }

    public static class IntGZip2Compressor extends GZip2Compressor<IntBuffer> {

        public IntGZip2Compressor() {
            super(ElementType.INT.size());
        }

        @Override
        protected void getPixel(IntBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void setPixel(IntBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class FloatGZip2Compressor extends GZip2Compressor<FloatBuffer> {

        public FloatGZip2Compressor() {
            super(ElementType.FLOAT.size());
        }

        @Override
        protected void getPixel(FloatBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void setPixel(FloatBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class LongGZip2Compressor extends GZip2Compressor<LongBuffer> {

        public LongGZip2Compressor() {
            super(ElementType.LONG.size());
        }

        @Override
        protected void getPixel(LongBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void setPixel(LongBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class DoubleGZip2Compressor extends GZip2Compressor<DoubleBuffer> {

        public DoubleGZip2Compressor() {
            super(ElementType.DOUBLE.size());
        }

        @Override
        protected void getPixel(DoubleBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void setPixel(DoubleBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class ShortGZip2Compressor extends GZip2Compressor<ShortBuffer> {

        public ShortGZip2Compressor() {
            super(ElementType.SHORT.size());
        }

        @Override
        protected void getPixel(ShortBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void setPixel(ShortBuffer pixelData, byte[] pixelBytes) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public GZip2Compressor(int primitiveSize) {
        super(primitiveSize);
    }

    private int[] calculateOffsets(byte[] byteArray) {
        int[] offset = new int[primitiveSize];
        offset[0] = 0;
        for (int primitivIndex = 1; primitivIndex < primitiveSize; primitivIndex++) {
            offset[primitivIndex] = offset[primitivIndex - 1] + byteArray.length / primitiveSize;
        }
        return offset;
    }

    @Override
    public boolean compress(T pixelData, ByteBuffer compressed) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void decompress(ByteBuffer compressed, T pixelData) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public byte[] shuffle(byte[] byteArray) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public byte[] unshuffle(byte[] byteArray) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
