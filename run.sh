#!/usr/bin/env bash
set -eo pipefail

case $1 in
  build)
    yarn run build
    ;;
  test)
    yarn test $@
    ;;
  *)
    exec $@
    ;;
esac
