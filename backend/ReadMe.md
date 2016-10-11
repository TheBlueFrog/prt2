
## General

## Oct 5 Demo/Presentation
#### Simulation
1. Model existing ad-hock distribution model for farm-restaurant. 

    Quantify the inefficiencies of the delivery cycle.
1. Model an organized distribution model for same.
    Quantify the inefficiencies of the delivery cycle.

1. First level quantification of improvement in deadtime

#####Defining the inefficiencies of the delivery cycle
1.  Existing model
    
    <p>Farm (home) pickup
    <p>Farm to first delivery
    <p>Delivery to delivery
    <p>Last delivery to farm (home)
    
1.  New model

    <p>Farm (home) pickup
    <p>Farm to farm pickup
    <p>Last pickup to first delivery
    <p>Delivery to delivery
    <p>Last delivery to farm (home)

    Depends on how many farm-to-farm pickups
    
#### Bot/Web/App/Exchange

<pre><code>
a   farmer A
b   farmer B

p - restaurant P
q - restaurant Q

T - transport exchange

a -> bot    "I will have 30 pounds of blueberries on Wednesday"
bot -> a    "Ok"

p -> bot    "I want 10 pounds of blueberries on Friday"
bot -> p    "OK, confirm 10 pounds of blueberry delivered on Friday?"
p -> bot    "Confirmed"
bot -> a    "Pickup 10 pounds of blueberries on Friday"

bot -> T    "124: 10 pounds blueberries from Loc<sub>a</sub> to Loc<sub>p</sub> on Friday"

b -> bot    "I have 50 bushes of corn now"
bot -> b    "Ok"

q -> bot    "I want 2 bushels of corn on Friday"
bot -> q    "OK, confirm 2 bushels of corn delivered on Friday?"
q -> bot    "Confirm"
bot -> b    "Pickup 2 bushels of corn on Friday"

bot -> T    "125: 2 bushels of corn from Loc<sub>b</sub> to Loc<sub>q</sub> on Friday"

a -> T      "bid <i>x</i> on 124 and 125"
T -> a      "OK, confirm deliver 124 and 125?"
a -> T      "Confirmed"

</code></pre>

## Issues

## ToDo
May, or may not, duplicate what's in the code @ToDo markers