#!/bin/bash

sudo apt-get update
sudo apt-get install build-essential libssl-dev

curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.11/install.sh | bash
source ~/.bashrc

nvm install 10.15.1
