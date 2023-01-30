
build-project:
	@echo "Build movie game"
	./gradlew clean build

start-app:
	@echo "Starting movie game"
	./gradlew clean bootRun

compose-down:
	@echo "Removing movie game database"
	docker-compose down --remove-orphans --volumes

compose-up:
	@echo "Starting movie game database"
	docker-compose up
