var request = require('request');
var myObj ={
  "topology": [
    {
      "Node_id": "A",
      "wlm_slots": [
        2
      ],
      "msmh_slots": [
        5
      ],
      "wlm_info": [
        {
          "slot_no": 2,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 1,
          "Wavelength": 45
        },
        {
          "slot_no": 2,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 2,
          "Wavelength": 46
        }
      ],
      "msmh_info": [
        {
          "slot_no": 5,
          "used_port": [
            1,
            2,
            3,
            4
          ]
        }
      ],
      "node_port": {
        "I": 2,
        "B": 1
      },
      "Node Type": "1"
    },
    {
      "Node_id": "B",
      "node_port": {
        "C": 1,
        "A":2
      },
      "Node Type": "0"
    },
    {
      "Node_id": "G",
      "node_port": {
        "C": 1,
        "F": 2,
        "H": 3
      },
      "Node Type": "0"
    },
    {
      "Node_id": "E",
      "node_port": {
        "D": 3,
        "C": 1,
        "I": 2
      },
      "Node Type": "0"
    },
    {
      "Node_id": "C",
      "wlm_slots": [
        2,
        3
      ],
      "msmh_slots": [
        5
      ],
      "wlm_info": [
        {
          "slot_no": 2,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 1,
          "Wavelength": 45
        },
        {
          "slot_no": 3,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 3,
          "Wavelength": 47
        },
        {
          "slot_no": 2,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 2,
          "Wavelength": 46
        },
        {
          "slot_no": 3,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 4,
          "Wavelength": 48
        }
      ],
      "msmh_info": [
        {
          "slot_no": 5,
          "used_port": [
            1,
            2,
            3,
            4
          ]
        }
      ],
      "node_port": {
        "D": 3,
        "G":1,
        "E":2,
        "B":4
      },
      "Node Type": "1"
    },
    {
      "Node_id": "D",
      "wlm_slots": [
        2,
        3
      ],
      "msmh_slots": [
        5
      ],
      "wlm_info": [
        {
          "slot_no": 2,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 1,
          "Wavelength": 45
        },
        {
          "slot_no": 2,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 2,
          "Wavelength": 46
        },
        {
          "slot_no": 3,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 3,
          "Wavelength": 47
        },
        {
          "slot_no": 3,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 4,
          "Wavelength": 48
        }
      ],
      "msmh_info": [
        {
          "slot_no": 5,
          "used_port": [
            1,
            2,
            3,
            4
          ]
        }
      ],
      "node_port": {
        "H": 3,
        "E": 2,
        "C": 1,
        "F":4
      },
      "Node Type": "1"
    },
    {
      "Node_id": "H",
      "wlm_slots": [
        2,
        3
      ],
      "msmh_slots": [
        5
      ],
      "wlm_info": [
        {
          "slot_no": 2,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 1,
          "Wavelength": 45
        },
        {
          "slot_no": 3,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 3,
          "Wavelength": 47
        },
        {
          "slot_no": 2,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 2,
          "Wavelength": 46
        }
      ],
      "msmh_info": [
        {
          "slot_no": 5,
          "used_port": [
            1,
            2,
            3,
            4
          ]
        }
      ],
      "node_port": {
        "D": 2,
        "G":1

      },
      "Node Type": "1"
    },
    {
      "Node_id": "I",
      "wlm_slots": [
        2
      ],
      "msmh_slots": [
        5
      ],
      "wlm_info": [
        {
          "slot_no": 2,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 1,
          "Wavelength": 45
        },
        {
          "slot_no": 2,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 2,
          "Wavelength": 46
        }
      ],
      "msmh_info": [
        {
          "slot_no": 5,
          "used_port": [
            1,
            2,
            3,
            4
          ]
        }
      ],
      "node_port": {
        "D": 2,
        "A": 1,
        "E": 3
      },
      "Node Type": "1"
    },
    {
      "Node_id": "F",
      "wlm_slots": [
        3,
        2
      ],
      "msmh_slots": [
        5
      ],
      "wlm_info": [
        {
          "slot_no": 2,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 1,
          "Wavelength": 45
        },
        {
          "slot_no": 3,
          "port": 1,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 3,
          "Wavelength": 47
        },
        {
          "slot_no": 2,
          "port": 2,
          "used_tributaries": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
          ],
          "Direction": 2,
          "Wavelength": 46
        }
      ],
      "msmh_info": [
        {
          "slot_no": 5,
          "used_port": [
            1,
            2,
            3,
            4
          ]
        }
      ],
      "node_port": {
        "D": 2,
        "G":1
      },
      "Node Type": "1"
    }
  ],
  "demand": [
    {
      "id": 1,
      "required_tributaries": 8,
      "Global_id": 1,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 2,
      "required_tributaries": 8,
      "Global_id": 2,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 1,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "E",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 3,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 4,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 5,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 6,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 7,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 8,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 9,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 10,
      "required_tributaries": 1,
      "Global_id": 3,
      "Traffic_type": 0,
      "Protection_type": 0,
      "Protection_id": 0,
      "path": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        }
      ]
    },
    {
      "id": 11,
      "required_tributaries": 8,
      "Global_id": 4,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 2,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 12,
      "required_tributaries": 8,
      "Global_id": 5,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 3,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 13,
      "required_tributaries": 8,
      "Global_id": 6,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 4,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 14,
      "required_tributaries": 8,
      "Global_id": 7,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 5,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 15,
      "required_tributaries": 8,
      "Global_id": 8,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 6,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 16,
      "required_tributaries": 8,
      "Global_id": 9,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 7,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 17,
      "required_tributaries": 8,
      "Global_id": 10,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 8,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 18,
      "required_tributaries": 8,
      "Global_id": 11,
      "Traffic_type": 1,
      "Protection_type": 1,
      "Protection_id": 9,
      "path1": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "B",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 1,
          "wavelength": 45
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        }
      ],
      "path2": [
        {
          "node_id": "A",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 2,
          "wavelength": 46
        },
        {
          "node_id": "F",
          "switch": 1,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 3,
          "wavelength": 47
        }
      ]
    },
    {
      "id": 19,
      "required_tributaries": 8,
      "Global_id": 12,
      "Traffic_type": 1,
      "Protection_type": 2,
      "Protection_id": 10,
      "path1": [
        {
          "node_id": "F",
          "switch": 0,
          "direction": 4,
          "wavelength": 48
        },
        {
          "node_id": "G",
          "switch": 0,
          "direction": 4,
          "wavelength": 48
        },
        {
          "node_id": "H",
          "switch": 0,
          "direction": 4,
          "wavelength": 48
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 5,
          "wavelength": 49
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 5,
          "wavelength": 49
        }
      ],
      "path2": [
        {
          "node_id": "F",
          "switch": 0,
          "direction": 5,
          "wavelength": 49
        },
        {
          "node_id": "I",
          "switch": 0,
          "direction": 5,
          "wavelength": 49
        },
        {
          "node_id": "E",
          "switch": 0,
          "direction": 5,
          "wavelength": 49
        },
        {
          "node_id": "D",
          "switch": 1,
          "direction": 6,
          "wavelength": 50
        },
        {
          "node_id": "C",
          "switch": 0,
          "direction": 6,
          "wavelength": 50
        }
      ]
    }
  ]
};
request({
  url: 'http://127.0.0.1:8081/client',
  method: 'POST',
  json: myObj,
}, function(error, response, body){
    console.log(response.body);
});


var express=require('express');
var  app=express();
var http =require('http');
http.Server(app).listen(8082);
var bodyParser = require('body-parser')
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())
app.post("/pro",function(request,response){
    var result=request.body;
    console.log(result);
    response.writeHead(200, {'Content-Type': 'application/JSON'});
    var x="data successfully recieved by client";
    response.write(x);
    response.end();

})