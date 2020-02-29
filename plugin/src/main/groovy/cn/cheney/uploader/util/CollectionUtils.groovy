package cn.cheney.uploader.util

class CollectionUtils {

    static boolean isNullOrEmpty(Collection collection) {
        return !collection || collection.size() < 1
    }

    static boolean isNotNullAndNotEmpty(Collection collection) {
        return !isNullOrEmpty(collection)
    }

}
