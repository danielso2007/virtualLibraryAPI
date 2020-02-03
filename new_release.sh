#!/bin/bash
RED='\033[1;31m'
BLACK='\033[0;30m'
DARK_GRAY='\033[1;30m'
LIGHT_RED='\033[0;31m'
GREEN='\033[0;32m'
LIGHT_GREEN='\033[1;32m'
BROWN_ORANGE='\033[0;33m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
LIGHT_BLUE='\033[1;34m'
PURPLE='\033[0;35m'
LIGHT_PURPLE='\033[1;35m'
CYAN='\033[0;36m'
LIGHT_CYAN='\033[1;36m'
LIGHT_GRAY='\033[0;37m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color
set -e
BRANCH=$1
VERSION=$2
DRYRUN=$3
if [[ -z "$1" ]] || [[ -z "$2" ]]; then
    echo -e "${RED}Usage:${NC} ${LIGHT_RED}./new_release.sh [BRANCH] [VERSION]${NC} or ${LIGHT_RED}./new_release.sh [BRANCH] [VERSION] [test - DRY-RUN]${NC}"
else
    echo -e "${YELLOW}Branch:${NC} ${LIGHT_PURPLE}${BRANCH}${NC}"
    echo -e "${YELLOW}Version:${NC} ${LIGHT_PURPLE}${VERSION}${NC}"
    echo -e "${YELLOW}GIT:${NC} ${LIGHT_PURPLE}git pull${NC}"
    git pull
    if [[ "$3" ]]; then
        echo -e "${YELLOW}npm:${NC} npm run release -- --release-as ${VERSION} --dry-run${NC}"
        npm run release -- --release-as ${VERSION} --dry-run
    else
        echo -e "${YELLOW}npm:${NC} npm run release -- --release-as ${VERSION}${NC}"
        npm run release -- --release-as ${VERSION}
        echo -e "${YELLOW}GIT:${NC} ${LIGHT_PURPLE}git push --follow-tags origin ${BRANCH}${NC}"
        git push --follow-tags origin ${BRANCH}
    fi
fi
