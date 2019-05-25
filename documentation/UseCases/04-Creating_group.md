# Creating the group

## Brief description
The user decides to create a group in order track friends geo-location. User initiates create group procedure and specifies the user credentials and group name. The system verifies the data and replies with the invitation key which user can share with his friends. After group is created, the user becomes a group owner

## Pre-conditions
- Connection to the server
- Completed user profile

## Post-conditions
- The new group is created
- User becomes a group owner

## Open questions
+ Do we need a group icon?
    + For the future version we can add this feature
+ Is the group name unique?
    + No, group will be identified by invitation key GroupName:RandNumber???
    

# 
## Scenario
- Username
- Icon or default generated from his username

>pressing the button `(Number 1)`

- in next screen user needs enter group name

>pressing the button `(Number 2)`

- Group is created and owner has invitation key
- By pressing copy button owner copies invitation link into clipboard

>pressing the button `(Number 3)`

- Owner transitioned to new group witch he made

### alternative flow

1. Username is missing
> Give error message "Fill required boxes"

2. User logo is missing
>Use default logo

3. Group name is missing
> Give error message "Fill required boxes"

# Transaction
![](https://i.imgur.com/WxSccR7.png)