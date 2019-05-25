package com.example.erasmusapp.Rest

import android.os.Parcel
import org.eproject.protocol.*;
import org.eproject.protocol.core.Opcode;
import org.eproject.protocol.helpers.InputBuffer;
import java.net.*;
import java.io.*;
import java.util.*
import java.util.logging.Logger;

class ApiCon() {
    /*Vlad server ip:142.93.168.179 and port 5432*/

    var IP_ADDRESS = "142.93.168.179"
    var PORT: Int = 5432
    val HEADER_LENGTH: Int = 5
    val clientSocket = Socket(IP_ADDRESS, PORT)

    private val logger = Logger.getLogger(ApiCon::class.java!!.name)
    //Members [org.eproject.protocol.MemberLocation@de2a027] []
    //Members [] [org.eproject.protocol.UserProfile@9272e2b]
    //ok i get it now

    @Throws(IOException::class)
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
            logger.info("Send message to server: OP_CREATE_GROUP")
        } catch (e: IOException) {
            logger.info("Error$e")
        }

        val buffer = ByteArray(HEADER_LENGTH)
        val inputBuffer = InputBuffer(buffer)
//so now when you are going to close the thing and try to join to aka:S11cE5cD6HSxmR4w86Ttow== okey
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

            logger.info("Invkey: $message")
            return message
        } catch (e: IOException) {
            return "error  $e"
        }
        return "No inv key"
    }

    @Throws(IOException::class, InterruptedException::class)
    fun joinGroup(invKey: String, name: String, foto: ByteArray , longitude : Double , latitude : Double): GroupState? {

        // this is sending part it is in kotlin but i have java version too
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

            logger.info("----------Joined To the groop------")
        } catch (e: IOException) {
            logger.info("" + e)
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
            logger.info("" + e)
        }

//ok so you join the group and then fetch the group data/ YES
        //ok so when other user joins it doesnt reflect the previously joined ones in the group data/YEs
        //ok show me the join code serverside
        //i didint use logger , let me try one more
        //I/ApiCon: Your user id is : 0
        //I/ApiCon: From server: OP_SUBSCRIBED
        //I/ApiCon: Members [org.eproject.protocol.MemberLocation@f433e88] []
        logger.info("Your user id is : " + subscribed.getUserId())
        logger.info("From server: " + serverHeader.getOpcode())

        when (serverHeader.getOpcode()) {
            Opcode.OP_SUBSCRIBED -> {
                val shareLocation = ShareLocation()
                shareLocation.setLocation(longitude , latitude)
                val sharelocation_payloadbuf = shareLocation.serialize()

                val shareLocationHeader = Header()
                shareLocationHeader.setPayloadSize(sharelocation_payloadbuf.size)
                shareLocationHeader.setOpcode(Opcode.OP_SHARE_LOCATION)

                val sharelocationHeaderBuf = shareLocationHeader.serialize()

                try {
                    dataOutputStream.write(sharelocationHeaderBuf)
                    dataOutputStream.write(sharelocation_payloadbuf)
                }catch (e: IOException){
                    logger.info("" + e)
                }



                val sh_Header = Header()
                val sh_buffer = ByteArray(HEADER_LENGTH)

                inputBuffer = InputBuffer(sh_buffer)

                val groupState = GroupState()

                try {
                    dataInputStream.read(sh_buffer)
                    sh_Header.deserialize(inputBuffer)

                    //[0,0,0,0,0,0]
                    val sh_payloadBuf = ByteArray(sh_Header.getPayloadSize())
                    //[5,4,6]
                    dataInputStream.readFully(sh_payloadBuf)

                    //[0,0,0,0,0]
                    groupState.deserialize(InputBuffer(sh_payloadBuf))

                }catch (e: IOException){
                    logger.info("" + e)
                }




                array = groupState
                logger.info("Members " + array.getLocations() + " " + array.newMembers)
               /* for(i in array.indices){
                    logger.info("Member id : " + array.get(i).memberId+ " latitude " + array.get(i).latitude +" longitude "+ array.get(i).longitude)
                }*/
                return array
            }
        }
        return null
    }

    @Throws(IOException::class, InterruptedException::class)
    fun shareLocation(longitude : Double , latitude : Double): MutableList<MemberLocation>? {
        val array: MutableList<MemberLocation>
        val dataInputStream = DataInputStream(clientSocket.getInputStream())
        val shareLocation = ShareLocation()
        shareLocation.setLocation(longitude, latitude)
        val sharelocation_payloadbuf = shareLocation.serialize()

        val shareLocationHeader = Header()
        shareLocationHeader.setPayloadSize(sharelocation_payloadbuf.size)
        shareLocationHeader.setOpcode(Opcode.OP_SHARE_LOCATION)

        val sharelocationHeaderBuf = shareLocationHeader.serialize()

        try {
            val dataOutputStream = DataOutputStream(clientSocket.getOutputStream())
            dataOutputStream.write(sharelocationHeaderBuf)
            dataOutputStream.write(sharelocation_payloadbuf)
        }catch (e: IOException){
            logger.info("" + e)
        }



        val sh_Header = Header()
        val sh_buffer = ByteArray(HEADER_LENGTH)

         val inputBuffer = InputBuffer(sh_buffer)

        val groupState = GroupState()

        try {
            dataInputStream.read(sh_buffer)
            sh_Header.deserialize(inputBuffer)
            val sh_payloadBuf = ByteArray(sh_Header.getPayloadSize())
            dataInputStream.read(sh_payloadBuf)
            groupState.deserialize(InputBuffer(sh_payloadBuf))

        }catch (e: IOException){
            logger.info("" + e)
        }
        array = groupState.getLocations()
        for(i in array.indices){
            logger.info("Member id : " + array.get(i).memberId+ " latitude " + array.get(i).latitude)
        }

        return array
    }
    @Throws(IOException::class, InterruptedException::class)
    fun groupStateUserProfile(longitude : Double , latitude : Double): MutableList<UserProfile>? {
        val array: MutableList<UserProfile>
        val dataInputStream = DataInputStream(clientSocket.getInputStream())
        val shareLocation = ShareLocation()
        shareLocation.setLocation(longitude, latitude)
        val sharelocation_payloadbuf = shareLocation.serialize()

        val shareLocationHeader = Header()
        shareLocationHeader.setPayloadSize(sharelocation_payloadbuf.size)
        shareLocationHeader.setOpcode(Opcode.OP_SHARE_LOCATION)

        val sharelocationHeaderBuf = shareLocationHeader.serialize()

        try {
            val dataOutputStream = DataOutputStream(clientSocket.getOutputStream())
            dataOutputStream.write(sharelocationHeaderBuf)
            dataOutputStream.write(sharelocation_payloadbuf)
        }catch (e: IOException){
            logger.info("" + e)
        }



        val sh_Header = Header()
        val sh_buffer = ByteArray(HEADER_LENGTH)

        val inputBuffer = InputBuffer(sh_buffer)

        val groupState = GroupState()

        try {
            dataInputStream.read(sh_buffer)
            sh_Header.deserialize(inputBuffer)
            val sh_payloadBuf = ByteArray(sh_Header.getPayloadSize())
            dataInputStream.read(sh_payloadBuf)
            groupState.deserialize(InputBuffer(sh_payloadBuf))

        }catch (e: IOException){
            logger.info("" + e)
        }
        array = groupState.newMembers

        return array
    }
    @Throws(IOException::class)
    fun handShaking(): Opcode? {
        val header = Header()
        val PROTOCOL_VERSION: Short = 0x1
        val hello = Hello()
        hello.setProtocolVersion(PROTOCOL_VERSION)

        val payloadbuf = hello.serialize()

        header.setOpcode(Opcode.OP_HELLO)
        header.setPayloadSize(payloadbuf.size)

        val headerBuf = header.serialize()

        val dataOutputStream = DataOutputStream(clientSocket.getOutputStream())

        try {
            dataOutputStream.write(headerBuf)
            dataOutputStream.write(payloadbuf)

        }catch (e: IOException){
            println("There was and error: $e")
        }


        val dataInputStream = DataInputStream(clientSocket.getInputStream())
        val message = ByteArray(HEADER_LENGTH)
        dataInputStream.read(message)

        header.deserialize(InputBuffer(message))

        logger.info("Mesage from server: " + header.getOpcode())
        val retrunOpCode = header.getOpcode()

        return retrunOpCode
    }
}
