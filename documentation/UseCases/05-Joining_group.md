# Joining the group
## Brief description
The user received the invitation key and initiates join group procedure by providing user id and invitation key to the system.
The system verifies the invitation key and adds user into the group.

## Pre-conditions
- Connect to the server
- Invitation key
- Completed user profile ( Name and picture)

## Post-conditions
- User becomes a group member

## Open questions



## Scenario
- Inserting invitation key
- Filling personal data ( Name , icon )

>pressing buton to join the group

- User join the group

>Seeing the map

- User send his location and get group member location



### alternative flow

1. User don't have internet connection ( Failled to establish internet connection)

> Give error message to check for internet connection 

2. User have internet connection but failed to establish secure connection (SSL)

> Give error message that failled to establish secure connection

3. User insert invalid Invitation key

> Give error message to check the key
> Give notification that group no longer exist

4. User join the group but Name already exist in the group
> Give error message that this name already in use 

5. User have same named picture

> Give error message that name of the picture are already in use

6. User fill Name and icon but lost Internet connection

> Give error message there is no connection with the server and redirect to starting page

