var T = 120000;
var FlightsPerPage = 8;
var PAGE_INTERVAL = 10000;
var CONNECT_INTERVAL = 5000;
var EMPTY_MESSAGE = '创新思维 智慧海航';
var url = 'ws://localhost:61614/stomp';
var login = '';
var passcode = '';

function buildClock() {
    var now = new Date();
    var date = now.format('yyyy年m月d日');
    var time = now.format('H:MM:ss');
    $('#date').text(date);
    $('#time').text(time);
    setInterval(function () {
        var date = now.format('yyyy年m月d日');
        var time = new Date().format('H:MM:ss');
        $('#date').text(date);
        $('#time').text(time);
    }, 1000);
}

var Move = function(startPosition, offset) {
    var dotPath = $('#dot-track');
    var baggage = $('.baggage');
    offset = (offset - 0.5) * baggage.height();
    var r0 = parseFloat(dotPath.css('border-radius'));      // original radius
    var l = dotPath.width() - 2 * r0;                       // width
    var h = dotPath.height() - 2 * r0;                      // height
    var r = r0 - offset;
    var o = 2 / T * (Math.PI + (l + h) / r0);               // angular speed
    var borderWidth = parseFloat(dotPath.css("border-width"));
    var xoffset = -1 + dotPath.position().left + parseFloat(dotPath.css("margin-left")) + borderWidth - baggage.width() / 2 + offset;
    var yoffset = -1 + dotPath.position().top + parseFloat(dotPath.css("margin-top")) + borderWidth - baggage.height() / 2 + offset;
    var v = r0 * o;                                         // line speed
    var d = Math.PI / (2 * o);
    // tracktunnels
    var d1 = 0.43 * T;
    var d2 = 0.43 * T;
    var d3 = 0.43 * T;
    var d4 = 0.43 * T;
    var d5 = 0.43 * T;
    // intervals
    var t1 = l / v;
    var t2 = t1 + d;
    var t3 = t2 + h / v;
    var t4 = t3 + d;
    var t5 = t4 + l / v;
    var t6 = t5 + d;
    var t7 = t6 + h / v;
    var t8 = t7 + d;
    this.css = function (t) {
        var x, y, angle;
        t = (t + startPosition) * T % T;
        if (t < t1) {
            angle = 0;
            x = v * t + r;
            y = 0;
        } else if (t < t2) {
            angle = o * t - l / r0;
            x = l + r * (1 + Math.sin(angle));
            y = r * (1 - Math.cos(angle));
        } else if (t < t3) {
            angle = Math.PI / 2;
            x = l + 2 * r;
            y = v * t + r - l - Math.PI * r0 * 0.5;
        } else if (t < t4) {
            angle = o * t - (l + h) / r0;
            x = l + r * (1 + Math.sin(angle));
            y = h + r * (1 - Math.cos(angle));
        } else if (t < t5) {
            angle = 0;
            x = 2 * l + h + r + Math.PI * r0 - v * t;
            y = 2 * r + h;
        } else if (t < t6) {
            angle = o * t - (2 * l + h) / r0;
            x = r * (1 + Math.sin(angle));
            y = h + r * (1 - Math.cos(angle));
        } else if (t < t7) {
            angle = Math.PI / 2;
            x = 0;
            y = 2 * (l + h) + r + Math.PI * r0 * 1.5 - v * t;
        } else if (t < t8) {
            angle = o * t - 2 * (l + h) / r0;
            x = r * (1 + Math.sin(angle));
            y = r * (1 - Math.cos(angle));
        }
        var rst = {top: yoffset + y + "px", left: xoffset + x + "px", transform: 'rotate(' + angle + 'rad)'};
        if (Math.abs(t-d1) < 10
            || Math.abs(t-d2) < 10
            || Math.abs(t-d3) < 10
            || Math.abs(t-d4) < 10
            || Math.abs(t-d5) < 10)
            rst.off = true;
        return rst;
    }
}

function setUpAnimation(id, start, offset) {
    $(id).stop().animate({path: new Move(start, offset)}, T, 'linear');
    setInterval(function() {
        $(id).stop().animate({path: new Move(start, offset)}, T, 'linear');
    }, T);
}


//////////////// FEATURE BEGIN: by situ 2013.7.23
var TRACKTUNNEL_TIMEOUT = 66666;

var tracktunnels = {};

function UpdateTrackTunnel(id, distance){

    function clearTunnels(){
        $('#tracktunnels').children().remove();
    }

    function addTunnel(id, distance){

        function distanceToPoint(distance){
            var dotPath = $('#dot-track');
            var baggage = $('.baggage');
            var ttheight = 50;
            var ttwidth = 100;
            var r0 = parseFloat(dotPath.css('border-radius'));      // original radius
            var l = dotPath.width() - 2 * r0;                       // width
            var h = dotPath.height() - 2 * r0;                      // height
            var r = r0;
            var o = 2 / T * (Math.PI + (l + h) / r0);               // angular speed
            var borderWidth = parseFloat(dotPath.css("border-width"));
            var xoffset = -1 + dotPath.position().left + parseFloat(dotPath.css("margin-left")) + borderWidth - ttwidth / 2;
            var yoffset = -1 + dotPath.position().top + parseFloat(dotPath.css("margin-top")) + borderWidth - ttheight / 2;
            var v = r0 * o;                                         // line speed
            var d = Math.PI / (2 * o);
            var t1 = l / v;
            var t2 = t1 + d;
            var t3 = t2 + h / v;
            var t4 = t3 + d;
            var t5 = t4 + l / v;
            var t6 = t5 + d;
            var t7 = t6 + h / v;
            var t8 = t7 + d;
            var x, y, angle;
            var t = distance * T % T;
            if (t < t1) {
                angle = 0;
                x = v * t + r;
                y = 0;
            } else if (t < t2) {
                angle = o * t - l / r0;
                x = l + r * (1 + Math.sin(angle));
                y = r * (1 - Math.cos(angle));
            } else if (t < t3) {
                angle = Math.PI / 2;
                x = l + 2 * r;
                y = v * t + r - l - Math.PI * r0 * 0.5;
            } else if (t < t4) {
                angle = o * t - (l + h) / r0;
                x = l + r * (1 + Math.sin(angle));
                y = h + r * (1 - Math.cos(angle));
            } else if (t < t5) {
                angle = 0;
                x = 2 * l + h + r + Math.PI * r0 - v * t;
                y = 2 * r + h;
            } else if (t < t6) {
                angle = o * t - (2 * l + h) / r0;
                x = r * (1 + Math.sin(angle));
                y = h + r * (1 - Math.cos(angle));
            } else if (t < t7) {
                angle = Math.PI / 2;
                x = 0;
                y = 2 * (l + h) + r + Math.PI * r0 * 1.5 - v * t;
            } else if (t < t8) {
                angle = o * t - 2 * (l + h) / r0;
                x = r * (1 + Math.sin(angle));
                y = r * (1 - Math.cos(angle));
            }
            angle -= Math.PI / 2;
            return {y: yoffset + y, x: xoffset + x, r: angle};
        };

        removeTunnel(id);

        var p = distanceToPoint(distance);

        var view = $('<img style="left: ' + p.x + 'px; top: ' + p.y + 'px; -webkit-transform: rotate(' + p.r + 'rad);' + '" src="images/tracktunnel.gif">');

        tracktunnels[id] = {
            view: view,
            timer: setTimeout(function(){
                removeTunnel(id);
            }, TRACKTUNNEL_TIMEOUT),
        }

        $('#tracktunnels').append(view);
    }

    function removeTunnel(id){
        if(tracktunnels[id]){
            clearTimeout(tracktunnels[id].timer);
            tracktunnels[id].view.remove();
            delete tracktunnels[id];
        }
    }

    addTunnel(id, distance);
}
//////////////// FEATURE END

var Baggage = Backbone.Model.extend({
    defaults: {
        'exists': true,
    }
});

var BaggageCollection = Backbone.Collection.extend({

    model: Baggage,

    initialize: function() {
        this.on("add", this.addBaggage);
        this.on("remove", this.removeBaggage);
        this.on("change", this.changeBaggage);
    },

    addBaggage: function(baggage) {
        var code = baggage.get("code");
        var start = baggage.get("start");
        var template = _.template($('#baggage-template').html());
        $("#" + baggage.id).remove();
        $("#baggages").append(template(baggage.toJSON()));
        Math.seedrandom(baggage.id);
        var offset = Math.random();
        console.log(baggage.id, offset);
        setUpAnimation("#" + baggage.id, start, offset);
    },

    removeBaggage: function(baggage) {
        $("#" + baggage.id).remove();
    },

    changeBaggage: function(baggage) {
        this.removeBaggage(baggage);
        this.addBaggage(baggage);
    }
});

var Flight = Backbone.Model.extend({
//////////////// BUG FIX: by situ 2013.7.22
    // defaults: {
    //     'baggages': new BaggageCollection()
    // }
    initialize: function() {
        this.set({'baggages': new BaggageCollection()});
    },

});

var FlightCollection = Backbone.Collection.extend({

    model: Flight,

    pagination : function(perPage, page) {
        page = page - 1;
        var collection = this;
        collection = _(collection.rest(perPage*page));
        collection = _(collection.first(perPage));    
        return collection.map(function (model) { return model.toJSON(); }); 
    }
});

var FlightItemView = Backbone.View.extend({

    template: _.template($('#flight-item-template').html()),

    render: function() {
       var html = this.template(this.model);
       this.setElement($(html));
       return this;
    }
});

var FlightView = Backbone.View.extend({

    tagName: 'ul',

    initialize: function() {
        this.collection.bind("all", this.render, this);
        this.currentPage = 1;
        $("#board").append(this.render().el);
    },

    nextPage: function() {
        if (++this.currentPage > this.totalPages()) {
            this.currentPage = 1;
        }
        this.collection.trigger("next_page");
    },

    totalPages: function() {
        return parseInt((this.collection.length - 1) / this.options.perPage + 1);
    },

    render: function() {
        this.$el.empty();
        this.$el.append($('#flight-header-template').html());
        var hasData = false;
        var flights = this.collection.pagination(this.options.perPage, this.currentPage);

        _.each(flights,
            function(flight) {
                var pageview = new FlightItemView({ model: flight });
                var $tr = pageview.render().$el;           
                this.$el.append($tr);
                hasData = true;
            },this);
        if (hasData) {
            this.$el.append('<li class="paginator"><span>'
                + this.currentPage + ' / ' + this.totalPages()
                + '</span></li>');
        } else {
            this.$el.append('<li class="empty"><span>暂无数据</span></li>'
                + '<li class="paginator"><span>0 / 0</span></li>');
        }
        return this;
    }
});

var Message = Backbone.Model.extend();

var MessageCollection = Backbone.Collection.extend({
    model: Message,

    initialize: function() {
        this.on("add", this.addMessage);
        this.on("change", this.changeMessage);
        this.timeouts = {};
    },

    addMessage: function(message) {
        var that = this;
        this.timeouts[message.id] = setTimeout(function() {
            that.remove(message);
        }, message.get('expire') - new Date().getTime());
    },

    changeMessage: function(message) {
        clearTimeout(this.timeouts[message.id]);
        this.addMessage(message);
    }
});

function getObject(msg) {
    return JSON.parse(msg.substr(msg.indexOf(',') + 1));
}

function getCode(id) {
    return id.split('/')[0];
}

function getDisplayCode(id) {
    return id.substr(0, id.length - 4);
}

function buildClient() {
    var VTrackTunnelUpdated = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VTrackTunnelUpdated";

    var VBaggageTracked = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VBaggageTracked";
    var VBaggageRemoved = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VBaggageRemoved";

    var VFlightUpdated = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VFlightUpdated";
    var VFlightRemoved = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VFlightRemoved";
    
    var VSyncRequest = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VSyncRequest";

    var VNotificationUpdated = 'com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VNotificationUpdated';
    var VNotificationRemoved = 'com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VNotificationRemoved';

    window.flightCollection = new FlightCollection();

    window.flightView = new FlightView({
        collection: flightCollection,
        perPage: FlightsPerPage
    });

    window.messageCollection = new MessageCollection();

//////////////// FEATURE BEGIN: by situ 2013.7.22
    (function(){

        var flights = {};

        flightCollection.on('add', function(flight){
            var baggages = flight.attributes.baggages;
            baggages.on('add', function(baggage){
                if(!flight.online){
                    flight.online = true;
                    flightCollection.add(flight);
                }
            });
            baggages.on('remove', function(baggage){
                if(baggages.length == 0){
                    flight.online = false;
                    flightCollection.remove(flight);
                }
            });
            if(baggages.length == 0){
                flight.online = false;
                flightCollection.remove(flight);
            }else{
                flight.online = true;
            }
            flights[flight.id] = flight;
        });

        flightCollection.on('remove', function(flight){
            delete flights[flight.id];
        });

        flightCollection.Get = function(id){
            return flights[id];
        }

    })();
//////////////// FEATURE END

    var client = Stomp.client(url);//建立stomp客户端
    var onconnect = function() {
        client.send('/topic/' + VSyncRequest, {}, 'com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VSyncRequest,{"component":0,"customClass":"","customJSON":"","messageId":"","name":"","needConfirm":false,"source":""}');

//////////////// FEATURE BEGIN: by situ 2013.7.23
        client.subscribe('/topic/' + VTrackTunnelUpdated, function(message) {
            var msg = getObject(message.body);
            UpdateTrackTunnel(msg.EPC, msg.distance);
        });
//////////////// FEATURE END

        client.subscribe('/topic/' + VBaggageTracked, function(message) {
            var msg = getObject(message.body);
            var flight = flightCollection.Get(msg.flightId);
            if (!flight) return;
            var baggage = new Baggage({
                id: msg.EPC,
                code: getCode(msg.flightId),
                start: msg.distance
            });
            flight.get("baggages").add(baggage, {merge: true});
        });
        client.subscribe('/topic/' + VBaggageRemoved, function(message) {
            var msg = getObject(message.body);
            var flight = flightCollection.Get(msg.flightId);
            if (!flight) return;
            var baggage = new Baggage({
                id: msg.EPC
            });
            flight.get("baggages").remove(baggage);
        });
        client.subscribe('/topic/' + VFlightUpdated, function(message) {
            var msg = getObject(message.body);
            var flight = new Flight({
                id: msg.flightId,
                number: getDisplayCode(msg.flightId),
                total: msg.total,
                sorted: msg.sorted,
                unsorted: msg.unsorted,
                missing: msg.missing,
                code: getCode(msg.flightId),
            });
            flightCollection.add(flight, {merge: true});
        });
        client.subscribe('/topic/' + VFlightRemoved, function(message) {
            var msg = getObject(message.body);
            var flight = new Flight({
                id: msg.flightId
            });
            flightCollection.remove(flight);
        });
        client.subscribe('/topic/' + VNotificationUpdated, function(obj) {
            var msg = getObject(obj.body);
            var message = new Message({
                id: msg.UUID,
                content: msg.content,
                time: msg.time,
                expire: msg.expire
            });
            if (msg.expire > new Date().getTime())
                messageCollection.add(message, {merge: true});
        });
        client.subscribe('/topic/' + VNotificationRemoved, function(obj) {
            var msg = getObject(message.body);
            var message = new Message({
                id: msg.UUID
            });
            messageCollection.remove(message);
        });
    };

    function startup() {
        setTimeout(function() {
            client.connect(login, passcode, onconnect, startup);//连接stomp服务器，此工程即ActiveMQ服务器
        }, CONNECT_INTERVAL);
    }
    startup();

    setInterval(function() {
        flightView.nextPage();
    }, PAGE_INTERVAL);

    var messageCounter = -1;
    $('#message').text(EMPTY_MESSAGE);
    $('marquee').marquee().on('start', function() {
        if (messageCollection.length == 0) {
            $('#message').text(EMPTY_MESSAGE);
            return;
        }
        if (++messageCounter >= messageCollection.length) {
            messageCounter = 0;
        }
        var message = messageCollection.at(messageCounter);
        var suffix = '';//' (' + new Date(message.get('time')).format('m月d日H:MM') + ')';
        $('#message').text(message.get('content') + suffix);
    });
}

function test() {

    var flightIds = [
        'HU7347',
        'HU7023',
        'HU7315',
        'HU7659',
        'HU7897',
        'HU7025',
        'HU8081',
        'HU7823',
        'HU6204',
        'HU7774',
        'HU7180',
        'HU7749',
        'HU7049',
        'HU7021',
        'HU7091',
        'HU6692',
        'HU7280',
        'HU7651',
        'HU7080',
        'HU7883',
        'HU7843',
        'HU7167',
        'HU7027',
        'HU7031',
    ];

    //for(var i = 0; i < 1; i += 0.01){
     //   UpdateTrackTunnel(i, i);
   // }

    for(var i = 0; i < flightIds.length; i++){
        var flightId = flightIds[i] + '/23JUL2013';
        var sorted = Math.round(Math.random() * 100 + 1);
        var unsorted = Math.round(Math.random() * 100 + 1);
        var missing = Math.round(Math.random() * 100 + 1);
        var flight = new Flight({
            id: flightId,
            number: getDisplayCode(flightId),
            total: sorted + unsorted + missing,
            sorted: sorted,
            unsorted: unsorted,
            missing: missing,
            code: getCode(flightId)
        });
        flightCollection.add(flight, {merge: true});
    }

   /* for(var i = 0; i < 40; i++){
        var flightId = flightIds[Math.round(Math.random() * 10)] + '/23JUL2013';
        var EPC = 'DS' + Math.round(Math.random() * 888888 + 111111);
        var distance = i * 0.05;
        var baggage = new Baggage({
            id: EPC,
            code: getCode(flightId),
            start: distance,
        });
        flightCollection.Get(flightId).get('baggages').add(baggage, {merge: true});
    }*/
}

buildClock();
buildClient();
test();