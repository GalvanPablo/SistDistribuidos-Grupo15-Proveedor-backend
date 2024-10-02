# Configuración y Ejecución de Kafka en Windows



## Requisitos Previos

1. **Descargar e instalar Windows Terminal** (opcional) para gestionar múltiples terminales. Puedes hacerlo desde Microsoft Store.

2. **Descargar Kafka**:
   - Accede a la [página de descarga de Kafka](https://kafka.apache.org/quickstart).
   - Descarga el archivo llamado `kafka_2.13-3.8.0.tgz` desde el siguiente enlace: [Descargar Kafka](https://dlcdn.apache.org/kafka/3.8.0/kafka_2.13-3.8.0.tgz).
   - Descomprime el archivo y coloca la carpeta en `C:\`, renombrándola a **"Kafka"**.

3. **Configuración e inicio**:
   - Una vez completados los pasos anteriores, ya estás listo para modificar la configuración e iniciar ZooKeeper y Kafka.
  
> **Nota:** En todas las terminales tenes que estar posicionado sobre la instalacion de kafka en tu sistema que generalmente es: `cd c:/Kafka`
> 

### Modificar Configuración para Windows

* Paso 1 - Editar archivo server.properties, cambiar log.dirs=/tmp/kafka-logs por log.dirs=c:/Kafka/kafka-logs
* Paso 2 - Editar archivo zookeeper.properties, cambiar dataDir=/tmp/zookeeper por dataDir=c:/Kafka/zookeeper-data


### Terminal 1: Iniciar Zookeeper
```bash
./bin/windows/zookeeper-server-start.bat ./config/zookeeper.properties
```

### Terminal 2: Iniciar Kafka
```bash
./bin/windows/kafka-server-start.bat ./config/server.properties
```

### Terminal 3: Crear un Nuevo Tópico (opcional para crear topicos por primera vez)
```bash
./bin/windows/kafka-topics.bat --create --topic {topic-name} --bootstrap-server localhost:9092
```

### Describir Detalles de un Tópico
```bash
./bin/windows/kafka-topics.bat --describe --topic {topic-name} --bootstrap-server localhost:9092
```

### Listar Todos los Tópicos
```bash
./bin/windows/kafka-topics.bat --list --bootstrap-server localhost:9092
```

## Opcional: Terminales para Enviar y Recibir Mensajes
---------------------------------------------------

### Terminal 4: Consola para Ver Mensajes de un Tópico (opcional para consumir mensajes de un topic)
```bash
./bin/windows/kafka-console-consumer.bat --topic {nombreTopic} --bootstrap-server localhost:9092
```
### Terminal 5: Consola para Enviar Mensajes a un Tópico (opcional para producir mensajes y mandarlos a un topic)
```bash
./bin/windows/kafka-console-producer.bat --broker-list localhost:9092 --topic {topic-name}
```
