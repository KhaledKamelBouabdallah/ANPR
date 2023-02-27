# ANPR
The android application that I developed is designed to provide a seamless and convenient experience for users who want to access private cities, parking spaces, and roads. With a user-friendly interface, the app enables users to choose from three different options: city, parking, or road. 
City: If the user selects the city option, they will need to enter the city's password to proceed. Once authorized, the user can take a photo of their car's number plate or enter the number manually. The app will extract the number and send it to a database. When the user's car arrives at the city's gate, a Raspberry-Pi system will scan the number plate with a camera to verify it against the database. If the number plate matches, the gate will open automatically.
Road: If the user selects the road option, they will need to choose the period during which they plan to use the road (e.g., a day, two days, a week, or a month). After that, they can take a photo of their car's number plate or enter the number manually. The app will calculate the cost based on the period chosen, and the user can pay using their phone. The number plate will be sent to a database, and when the car arrives at the gate, a Raspberry-Pi system will scan the number plate to verify it against the database. If the number plate matches, the gate will open automatically.
Parking: If the user selects the parking option, they will need to specify the day and time they plan to arrive at the parking lot. After that, they can take a photo of their car's number plate or enter the number manually. The app will calculate the cost, and the user can pay 10% of the total amount using their phone. The number plate will be sent to a database, and when the car arrives at the parking lot, a Raspberry-Pi system will scan the number plate to verify it against the database. If the number plate matches, the gate will open automatically.
Overall, this android application provides a convenient and hassle-free way for users to access private cities, parking spaces, and roads. With advanced technology, including image recognition and database integration, users can be assured of a secure and efficient experience.
