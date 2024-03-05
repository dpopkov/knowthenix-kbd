## How to run

### Docker образ через плагин Ktor-а

* `buildImage` - собирает образ в tarball, генерирует `jib-image.tar` в `build` директории,
  можно затем загрузить этот образ в Docker daemon: `docker load < build/jib-image.tar`
* `publishImageToLocalRegistry` - собирает образ и публикует его в локальный репозиторий
* `runDocker` - собирает образ в Docker daemon и запускает его. По умолчанию запустит Ktor на `0.0.0.0:8080`
* `publishImage` - собирает образ и публикует его во внешний registry (например DockerHub)
