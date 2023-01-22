MIGRATION_LABEL = "to-be-changed"
DATE_WITH_TIME := $(shell /bin/date "+%Y-%m-%d-%H:%M:%S")

banner:
	curl --get \
		 --data-urlencode "text=Awesome Movie Game" \
	     --data-urlencode "font=soft" \
	     "https://devops.datenkollektiv.de/renderBannerTxt" >> src/main/resources/banner.txt

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

generate-migration:
	@echo "Generating database migration"
	export CHANGELOG_FILE_NAME=${DATE_WITH_TIME}-${MIGRATION_LABEL}.sql && ./gradlew diffChangeLog -PrunList=diffMain

migrate-database:
	@echo "Migrating database"
	./gradlew update -PrunList=main
