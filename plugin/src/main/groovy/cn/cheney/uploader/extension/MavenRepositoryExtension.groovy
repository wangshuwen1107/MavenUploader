package cn.cheney.uploader.extension

class MavenRepositoryExtension  {

    String url

    boolean auth = true

    String userName

    String password


    @Override
    String toString() {
        return "MavenRepositoryExtension{" +
                "url='" + url + '\'' +
                ", auth=" + auth +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}'
    }
}