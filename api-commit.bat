git add api-example
set /p commitmsg=Enter Commit Message: 
git commit -am "%commitmsg%"
git push --force origin mvn-repo
pause