import bluetooth
import win32com.client
import win32api, win32con

vk_code={
    'esc':0x1B,
    'right':0x27,
    'left':0x25,
    'skey':0x53,
}

ppt = win32com.client.Dispatch("Powerpoint.Application")
ppt.Visible = True
pptfile = ppt.Presentations.Open('C:/Users/shantan/Downloads/36CSE5306Lecture20170424.pptx',ReadOnly=0, Untitled=0, WithWindow=1)#Open the desired ppt file
nSlides = pptfile.Slides.count
exit_flag = 0
size = 1024

uuid="00001101-0000-1000-8000-00805f9b34fb"

def runServer():
    serverSocket=bluetooth.BluetoothSocket(bluetooth.RFCOMM )
    port=5
    serverSocket.bind(("",port))
    print("Listening for connections on port: "+str(port))
    serverSocket.listen(1)
    port=serverSocket.getsockname()[1]
    exit_flag = 0

    bluetooth.advertise_service( serverSocket, "SampleServer",
                       service_id = uuid,
                       service_classes = [ uuid, bluetooth.SERIAL_PORT_CLASS ],
                       profiles = [ bluetooth.SERIAL_PORT_PROFILE ] 
                        )

    while 1:
        client, address = serverSocket.accept()
        data = client.recv(size)
        true_data = data.decode()
        if "forward" in true_data:
            split_data = true_data.split(" ")
            win32api.keybd_event(vk_code['right'],0,0,0)
            win32api.keybd_event(vk_code['right'],0,win32con.KEYEVENTF_KEYUP,0)
            client.send(str(int(split_data[1])+1).encode())
            client.close()
        elif "back" in true_data:
            split_data = true_data.split(" ")
            win32api.keybd_event(vk_code['left'],0,0,0)
            win32api.keybd_event(vk_code['left'],0,win32con.KEYEVENTF_KEYUP,0)
            client.send(str(int(split_data[1])-1).encode())
            client.close()
        elif "goto" in true_data:
            split_data = true_data.split(" ")
            if exit_flag == 0:
                ppt.ActiveWindow.View.GoToSlide(int(split_data[1]))
                client.send(str(int(split_data[1])).encode())
                client.close()
            else:
                ppt.SlideShowWindows(1).View.GoToSlide(int(split_data[1]))
                client.send(str(int(split_data[1])).encode())
                client.close()
        elif "slideshow" in true_data:
            if exit_flag != 1:
                ppt.ActivePresentation.SlideShowSettings.Run()
                exit_flag = 1
                client.send("1".encode())
                client.close()
            else:
                index = ppt.SlideShowWindows(1).View.Slide.SlideIndex
                client.send(str(index).encode())
                client.close()
        elif "getdata" in true_data:
            index = ppt.ActiveWindow.View.Slide.SlideIndex
            client.send((str(nSlides)+" "+str(index)).encode())
            client.close()
        elif "first" in true_data:
            client.close()
        else:
            win32api.keybd_event(vk_code['esc'],0,0,0)
            win32api.keybd_event(vk_code['esc'],0,win32con.KEYEVENTF_KEYUP,0)
            exit_flag = 0
            client.close()

runServer() 