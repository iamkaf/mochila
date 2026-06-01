@echo off
setlocal EnableExtensions EnableDelayedExpansion

set "TEAKIT_RUNNER_PINNED_VERSION=0.1.0-SNAPSHOT"
set "TEAKIT_GROUP_PATH=com/iamkaf/teakit"
set "TEAKIT_GROUP_PATH_WINDOWS=com\iamkaf\teakit"
set "TEAKIT_ARTIFACT=teakit-runner"

if not "%TEAKIT_RUNNER_JAR%"=="" (
  java -jar "%TEAKIT_RUNNER_JAR%" %*
  exit /b %ERRORLEVEL%
)

if "%TEAKIT_MAVEN_RELEASES%"=="" set "TEAKIT_MAVEN_RELEASES=https://z.kaf.sh/releases"
if "%TEAKIT_MAVEN_SNAPSHOTS%"=="" set "TEAKIT_MAVEN_SNAPSHOTS=https://z.kaf.sh/snapshots"
if "%TEAKIT_MAVEN_LOCAL%"=="" set "TEAKIT_MAVEN_LOCAL=%USERPROFILE%\.m2\repository"
if "%TEAKIT_CACHE_DIR%"=="" (
  set "TEAKIT_CACHE_ROOT=%USERPROFILE%\.cache\teakit"
) else (
  set "TEAKIT_CACHE_ROOT=%TEAKIT_CACHE_DIR%"
)

set "TEAKIT_WRAPPER_UPGRADE=0"
if not "%TEAKIT_RUNNER_VERSION%"=="" (
  set "TEAKIT_RUNNER_RESOLVED_VERSION=%TEAKIT_RUNNER_VERSION%"
  if /I "%~1"=="upgrade" set "TEAKIT_WRAPPER_UPGRADE=1"
) else if /I "%~1"=="upgrade" (
  set "TEAKIT_WRAPPER_UPGRADE=1"
  for /f "usebackq delims=" %%V in (`powershell -NoProfile -ExecutionPolicy Bypass -Command "$releases = '%TEAKIT_MAVEN_RELEASES%'; $snapshots = '%TEAKIT_MAVEN_SNAPSHOTS%'; $group = '%TEAKIT_GROUP_PATH%'; $artifact = '%TEAKIT_ARTIFACT%'; $urls = @($releases + '/' + $group + '/' + $artifact + '/maven-metadata.xml', $snapshots + '/' + $group + '/' + $artifact + '/maven-metadata.xml'); foreach ($url in $urls) { try { [xml]$metadata = Invoke-WebRequest -UseBasicParsing -Uri $url; $versioning = $metadata.metadata.versioning; if ($versioning.release) { Write-Output $versioning.release; exit 0 }; if ($versioning.latest) { Write-Output $versioning.latest; exit 0 }; $last = @($versioning.versions.version) | Select-Object -Last 1; if ($last) { Write-Output $last; exit 0 } } catch {} }; exit 1"`) do set "TEAKIT_RUNNER_RESOLVED_VERSION=%%V"
  if "!TEAKIT_RUNNER_RESOLVED_VERSION!"=="" (
    echo teakitw: could not resolve latest TeaKit runner from Kaf Maven
    echo teakitw: set TEAKIT_RUNNER_VERSION=^<version^> to upgrade to an explicit version
    exit /b 1
  )
) else (
  set "TEAKIT_RUNNER_RESOLVED_VERSION=%TEAKIT_RUNNER_PINNED_VERSION%"
)

set "DIST_DIR=%TEAKIT_CACHE_ROOT%\wrapper\dists\%TEAKIT_ARTIFACT%\%TEAKIT_RUNNER_RESOLVED_VERSION%"
set "JAR_PATH=%DIST_DIR%\%TEAKIT_ARTIFACT%.jar"

if /I not "%TEAKIT_MAVEN_LOCAL%"=="false" (
  set "LOCAL_JAR=%TEAKIT_MAVEN_LOCAL%\%TEAKIT_GROUP_PATH_WINDOWS%\%TEAKIT_ARTIFACT%\%TEAKIT_RUNNER_RESOLVED_VERSION%\%TEAKIT_ARTIFACT%-%TEAKIT_RUNNER_RESOLVED_VERSION%.jar"
  if exist "%LOCAL_JAR%" (
    if "%TEAKIT_WRAPPER_UPGRADE%"=="1" (
      echo teakitw: TeaKit runner %TEAKIT_RUNNER_RESOLVED_VERSION% is available from Maven local: "%LOCAL_JAR%"
      exit /b 0
    )
    java -jar "%LOCAL_JAR%" %*
    exit /b %ERRORLEVEL%
  )
)

if exist "%JAR_PATH%" goto run

mkdir "%DIST_DIR%" 2>nul
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "$version = '%TEAKIT_RUNNER_RESOLVED_VERSION%';" ^
  "$releases = '%TEAKIT_MAVEN_RELEASES%';" ^
  "$snapshots = '%TEAKIT_MAVEN_SNAPSHOTS%';" ^
  "$group = '%TEAKIT_GROUP_PATH%';" ^
  "$artifact = '%TEAKIT_ARTIFACT%';" ^
  "$out = '%JAR_PATH%';" ^
  "$base = if ($version.EndsWith('-SNAPSHOT')) { $snapshots } else { $releases };" ^
  "$resolved = $version;" ^
  "if ($version.EndsWith('-SNAPSHOT')) {" ^
  "  [xml]$metadata = Invoke-WebRequest -UseBasicParsing -Uri ($base + '/' + $group + '/' + $artifact + '/' + $version + '/maven-metadata.xml');" ^
  "  $snap = $metadata.metadata.versioning.snapshotVersions.snapshotVersion | Where-Object { $_.extension -eq 'jar' -and -not $_.classifier } | Select-Object -First 1;" ^
  "  if (-not $snap) { throw 'snapshot metadata did not contain a runnable jar' };" ^
  "  $resolved = $snap.value;" ^
  "}" ^
  "$url = $base + '/' + $group + '/' + $artifact + '/' + $version + '/' + $artifact + '-' + $resolved + '.jar';" ^
  "Invoke-WebRequest -UseBasicParsing -Uri $url -OutFile $out"
if errorlevel 1 (
  echo teakitw: failed to download TeaKit runner from Kaf Maven
  echo teakitw: set TEAKIT_RUNNER_JAR=C:\path\to\teakit-runner.jar for a local development runner
  exit /b 1
)

:run
if "%TEAKIT_WRAPPER_UPGRADE%"=="1" (
  echo teakitw: TeaKit runner %TEAKIT_RUNNER_RESOLVED_VERSION% is available at "%JAR_PATH%"
  exit /b 0
)

java -jar "%JAR_PATH%" %*
