package immutableVector.sizeNotInBuffer.fixedHeight.growableBufferSize.bufferSize32

internal const val MAX_BUFFER_SIZE = 32
internal const val LOG_MAX_BUFFER_SIZE = 5
internal const val MAX_BUFFER_SIZE_MINUS_ONE = MAX_BUFFER_SIZE - 1
internal const val SHIFT_START = 30  /// 31 - LOG_MAX_BUFFER_SIZE <= SHIFT_START < 31 && SHIFT_START % LOG_MAX_BUFFER_SIZE == 0
internal const val REST_HEIGHT = SHIFT_START / LOG_MAX_BUFFER_SIZE + 1