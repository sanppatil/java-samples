# Build Steps

### 1. Gradle build
```bash
gradle clean build
```

### 2. Docker build 
```bash
docker build -t enodation/spring-on-docker .
```

### 3. Run container 

#### Option a - Execute container background
```bash
docker run -d -p 8080:8080 --name enodation-spring-on-docker enodation/spring-on-docker
```

#### Option b - Execute container forground
```bash
docker run -p 8080:8080 --name enodation-spring-on-docker enodation/spring-on-docker
```

### 4. Go inside container for troubleshooting
```bash
docker exec -it enodation-spring-on-docker /bin/sh
```

### 5. Remove docker container

*Use -f switch in case container is in running state.*

```bash
docker rm -f enodation-spring-on-docker
```

### 5. Remove docker image
```bash
docker rmi enodation/spring-on-docker -f
```