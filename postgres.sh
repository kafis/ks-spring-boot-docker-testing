#!/usr/bin/env bash
docker run -e POSTGRES_PASSWORD=s3cr3t -e POSTGRES_USER=pollservice -d -p 5432:5432 postgres