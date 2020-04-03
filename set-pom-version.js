const { spawnSync } = require('child_process');
var packageJson = require('./package.json');

spawnSync('mvn', [
   'versions:set',
   '-DnewVersion=' + packageJson.version,
   '-DprocessAllModules=true',
   '-DgenerateBackupPoms=false'
], { stdio: 'inherit' });
