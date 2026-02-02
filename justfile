set shell := ["bash", "-cu"]

default:
  @just --list

# Resolved at runtime so new version folders are picked up automatically.
version := shell("ls -1d */ | sed 's:/$::' | grep -E '^[0-9]' | sort -V | tail -n1")

list-versions:
  @ls -1d */ | sed 's:/$::' | grep -E '^[0-9]' | sort -V

versions: list-versions

latest:
  @echo {{version}}

# Run arbitrary Gradle tasks.
# - If the first arg is a version directory, run only there.
# - Otherwise run across all versions.
run first="" *rest:
  @if [ -z "{{first}}" ]; then echo "Usage: just run [version] <gradle args>"; exit 1; fi
  @if [ -d "{{first}}" ] && echo "{{first}}" | grep -Eq '^[0-9]'; then \
    if [ -z "{{rest}}" ]; then echo "Usage: just run [version] <gradle args>"; exit 1; fi; \
    (cd "{{first}}" && ./gradlew {{rest}}); \
  else \
    for v in $(ls -1d */ | sed 's:/$::' | grep -E '^[0-9]' | sort -V); do \
      echo "==> $v"; (cd "$v" && ./gradlew {{first}} {{rest}}); \
    done; \
  fi

build version="":
  @if [ -z "{{version}}" ]; then \
    for v in $(ls -1d */ | sed 's:/$::' | grep -E '^[0-9]' | sort -V); do \
      echo "==> $v"; \
      for loader in fabric forge neoforge; do \
        (cd "$v" && ./gradlew :$loader:build); \
      done; \
    done; \
  else \
    if [ ! -d "{{version}}" ]; then echo "Version {{version}} not found."; exit 1; fi; \
    for loader in fabric forge neoforge; do \
      (cd "{{version}}" && ./gradlew :$loader:build); \
    done; \
  fi

test version="":
  @if [ -z "{{version}}" ]; then \
    for v in $(ls -1d */ | sed 's:/$::' | grep -E '^[0-9]' | sort -V); do \
      echo "==> $v"; (cd "$v" && ./gradlew test); \
    done; \
  else \
    if [ ! -d "{{version}}" ]; then echo "Version {{version}} not found."; exit 1; fi; \
    (cd "{{version}}" && ./gradlew test); \
  fi

changed base="origin/main":
  @if ! git rev-parse --verify "{{base}}" >/dev/null 2>&1; then echo "Base ref {{base}} not found."; exit 1; fi
  @changed=$(git diff --name-only "{{base}}"...HEAD | grep -oP '^[0-9]+\\.[0-9]+[^/]*' | sort -u); \
  if [ -z "$changed" ]; then echo "No changed versions."; exit 0; fi; \
  echo "$changed"

build-changed base="origin/main":
  @if ! git rev-parse --verify "{{base}}" >/dev/null 2>&1; then echo "Base ref {{base}} not found."; exit 1; fi
  @changed=$(git diff --name-only "{{base}}"...HEAD | grep -oP '^[0-9]+\\.[0-9]+[^/]*' | sort -u); \
  if [ -z "$changed" ]; then echo "No changed versions."; exit 0; fi; \
  for v in $changed; do \
    echo "==> $v"; \
    for loader in fabric forge neoforge; do \
      (cd "$v" && ./gradlew :$loader:build); \
    done; \
  done

test-changed base="origin/main":
  @if ! git rev-parse --verify "{{base}}" >/dev/null 2>&1; then echo "Base ref {{base}} not found."; exit 1; fi
  @changed=$(git diff --name-only "{{base}}"...HEAD | grep -oP '^[0-9]+\\.[0-9]+[^/]*' | sort -u); \
  if [ -z "$changed" ]; then echo "No changed versions."; exit 0; fi; \
  for v in $changed; do \
    echo "==> $v"; (cd "$v" && ./gradlew test); \
  done

# Copy an existing version folder to create a new one.
new-version from to:
  @if [ -e "{{to}}" ]; then echo "Target {{to}} already exists."; exit 1; fi
  cp -r "{{from}}" "{{to}}"
