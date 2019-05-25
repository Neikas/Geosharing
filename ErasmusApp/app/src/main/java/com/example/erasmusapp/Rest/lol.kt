package com.example.erasmusapp.Rest

import org.eproject.protocol.*
import org.eproject.protocol.core.Opcode
import org.eproject.protocol.helpers.InputBuffer
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import java.util.*

var IP_ADDRESS = "192.168.1.88"
var PORT: Int = 5432 //bad port i Bad key
val HEADER_LENGTH: Int = 5
class Connection{
    val clientSocket = Socket(IP_ADDRESS, PORT)
    fun close(){
        clientSocket.close()
    }
    fun createGroup(groupName: String): String {
        val header = Header()
        val groupCreate = CreateGroup()
        groupCreate.setGroupName(groupName)
        val payload = groupCreate.serialize()

        header.setOpcode(Opcode.OP_CREATE_GROUP)
        header.setPayloadSize(payload.size)
        val headerbuf = header.serialize()

        val dataOutputStream = DataOutputStream(clientSocket.getOutputStream())
        try {
            dataOutputStream.write(headerbuf)
            dataOutputStream.write(payload)
        } catch (e: IOException) {
        }

        val buffer = ByteArray(HEADER_LENGTH)
        val inputBuffer = InputBuffer(buffer)

        val inputStream = clientSocket.getInputStream()
        val dataInputStream = DataInputStream(inputStream)

        val groupCreated = GroupCreated()
        try {
            dataInputStream.read(buffer)
            header.deserialize(inputBuffer)
            val payloadbuf = ByteArray(header.getPayloadSize())
            dataInputStream.read(payloadbuf)
            groupCreated.deserialize(InputBuffer(payloadbuf))


            val message = groupCreated.getInvitationKey()
            println("message = ${message}")
            return message
        } catch (e: IOException) {
            return "error  $e"
        }
        return "No inv key"
    }


    fun joinGroup(invKey: String, name: String, foto: ByteArray , longitude : Double , latitude : Double): GroupState? {

        // this is sending part it is in kotlin but i have java version too
        //we need to get good inv key
        val header = Header()
        val subscribe = Subscribe()
        val array: GroupState

        subscribe.setInvitationKey(invKey)
        subscribe.setImageData(foto)
        subscribe.setUserName(name)

        val payloadbuf = subscribe.serialize()

        header.setOpcode(Opcode.OP_SUBSCRIBE)
        header.setPayloadSize(payloadbuf.size)

        val headerBuffer = header.serialize()

        val dataOutputStream = DataOutputStream(clientSocket.getOutputStream())
        try {


            dataOutputStream.write(headerBuffer)
            dataOutputStream.write(payloadbuf)


        } catch (e: IOException) {
            e.printStackTrace()
        }

        val serverHeader = Header()
        val buffer = ByteArray(HEADER_LENGTH)
        var inputBuffer = InputBuffer(buffer)

        val dataInputStream = DataInputStream(clientSocket.getInputStream())

        val subscribed = Subscribed()
        try{
            dataInputStream.read(buffer)
            serverHeader.deserialize(inputBuffer)
            val payloadbuff = ByteArray(serverHeader.getPayloadSize())
            dataInputStream.read(payloadbuff)
            subscribed.deserialize(InputBuffer(payloadbuff))
        }catch (e: IOException){
            e.printStackTrace()
        }



        when (serverHeader.getOpcode()) {
            Opcode.OP_SUBSCRIBED -> {
                val shareLocation = ShareLocation()
                shareLocation.setLocation(longitude, latitude)
                val sharelocation_payloadbuf = shareLocation.serialize()

                val shareLocationHeader = Header()
                shareLocationHeader.setPayloadSize(sharelocation_payloadbuf.size)
                shareLocationHeader.setOpcode(Opcode.OP_SHARE_LOCATION)

                val sharelocationHeaderBuf = shareLocationHeader.serialize()

                try {
                    dataOutputStream.write(sharelocationHeaderBuf)
                    dataOutputStream.write(sharelocation_payloadbuf)
                }catch (e: IOException){
                    e.printStackTrace()
                }



                val sh_Header = Header()
                val sh_buffer = ByteArray(HEADER_LENGTH)

                inputBuffer = InputBuffer(sh_buffer)

                val groupState = GroupState()

                try {
                    dataInputStream.read(sh_buffer)
                    sh_Header.deserialize(inputBuffer)
                    val sh_payloadBuf = ByteArray(sh_Header.getPayloadSize())
                    dataInputStream.read(sh_payloadBuf)
                    groupState.deserialize(InputBuffer(sh_payloadBuf))

                }catch (e: IOException){
                    e.printStackTrace()
                }




                array = groupState
                //hmm so, can you point me out to the error here , look i will lode app we can see from the log
                //ok sec
                //ohhhh you do it here
                //can i show you the log offf app
                //ok/ yes so fixed?let me try agian
                //so this result is proper. ok now join
                println("array = ${array.newMembers} ${array.locations}")
                //so this is also proper
                /* for(i in array.indices){
                     logger.info("Member id : " + array.get(i).memberId+ " latitude " + array.get(i).latitude +" longitude "+ array.get(i).longitude)
                 }*/
                return array
            }
        }
        return null
    }

}

fun main() {
    //can you satisfy the params without runing the app/
    //its fine well refactor it here in kotlin, just provide the params so i can exec the function
    //kotlin :D
     //just array filled wiht 1/yes
    val firstLaunchBehavior = false

    //you pass the inv key on the second try how?
    //inserting it mt hand ok
    //okey you mimic code , but the way is diffrent that i get group state not call it in
    //no sorry. i was wrong
    //no join with need app ? sec
    val image = ByteArray(100){1}
    val groupState = GroupState()
    groupState.addMemberLocation(MemberLocation())
    groupState.addMemberLocation(MemberLocation())
    val groupState2 = GroupState()

    var payload = groupState.serialize();
    try {
        println("payload = ${payload.size}")
        val sh_payloadBuf = ByteArray(payload.size)
        groupState2.deserialize(InputBuffer(sh_payloadBuf))

    }catch (e: IOException){
        e.printStackTrace()
    }
    println("groupState = ${groupState2.locations}")
}