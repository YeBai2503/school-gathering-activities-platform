const options = {
    indexAxis: 'y', // 使条形图为垂直方向
    responsive: true,
    maintainAspectRatio: true, // 允许自定义宽高
    aspectRatio: 1, // 设置纵横比，调小这个值会使图表整体高度变小
    scales: {
        x: {
         
            beginAtZero: true, // X轴从零开始
            ticks: {
                color: 'white', // Y轴值的颜色设置为白色
                font: {
                    size: 15 // Y轴字体大小设置为20px
                }
            },
            grid: {
                color: 'rgba(255, 255, 255, 0.1)', // 网格线颜色可选
            }
        },
        y: {
            ticks: {
                color: 'white', // Y轴值的颜色设置为白色
                font: {
                    size: 15 // Y轴字体大小设置为20px
                }
            },
            grid: {
                color: 'rgba(255, 255, 255, 0.1)', // 网格线颜色可选
            },
        }
    },
    plugins: {
        legend: {
            display: true,
            position: 'top', // 图例位置
            labels: {
                font: {
                    size: 15, // 字体大小更改为 20px
                    family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif", // 字体家族
                    weight: 'bold', // 字体粗细
                    style: 'normal', // 字体样式
                    lineHeight: 1.5, // 行高
                },
                color: 'white', // 标签颜色设置为白色
            }
        },
        datalabels: {
            color: 'white', // 数据标签颜色
            anchor: 'end',  // 标签位置，end 表示在条形的尾端
            align: 'end',   // 标签的对齐方式
            font: {
                size: 20, // 数据标签字体大小
                weight: 'bold'
            },
            formatter: (value) => {
                return value; // 显示的具体内容，这里是数值
            }
        }
    }
};

const options2 = {
    indexAxis: 'x', // 使条形图为垂直方向
    responsive: true,
    maintainAspectRatio: true, // 允许自定义宽高
    aspectRatio: 1.1, // 可选，设置纵横比，调小这个值会使图表整体高度变小
    scales: {
        x: {
         
            beginAtZero: true, // X轴从零开始
            ticks: {
                color: 'white', // Y轴值的颜色设置为白色
                font: {
                    size: 15 // Y轴字体大小设置为20px
                }
            },
            grid: {
                color: 'rgba(255, 255, 255, 0.1)', // 网格线颜色可选
            }
        },
        y: {
            ticks: {
                color: 'white', // Y轴值的颜色设置为白色
                font: {
                    size: 15 // Y轴字体大小设置为20px
                }
            },
            grid: {
                color: 'rgba(255, 255, 255, 0.1)', // 网格线颜色可选
            },
        }
    },
    plugins: {
        legend: {
            display: true,
            position: 'top', // 图例位置
            labels: {
                font: {
                    size: 15, // 字体大小更改为 20px
                    family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif", // 字体家族
                    weight: 'bold', // 字体粗细
                    style: 'normal', // 字体样式
                    lineHeight: 1.5, // 行高
                },
                color: 'white', // 标签颜色设置为白色
            }
        },
        datalabels: {
            color: 'white', // 数据标签颜色
            anchor: 'end',  // 标签位置，end 表示在条形的尾端
            align: 'end',   // 标签的对齐方式
            font: {
                size: 20, // 数据标签字体大小
                weight: 'bold'
            },
            formatter: (value) => {
                return value; // 显示的具体内容，这里是数值
            }
        }
    }
    
};


const options3 = {
    responsive: true,
    plugins: {
        legend: {
            display: true,
            position: 'top',
            labels: {
                color: 'white', // 图例颜色
            }
        },
        datalabels: {
            color: 'white', // 数据标签字体颜色
            formatter: (value, context) => {
                const total = context.dataset.data.reduce((acc, val) => acc + val, 0);
                const percentage = ((value / total) * 100).toFixed(2) + '%'; // 计算百分比
                return percentage; // 返回百分比值
            },
            anchor: 'end',
            align: 'end',
            font: {
                size: 16,
                weight: 'bold',
            }
        }
    }
};

// 分类数量
// 获取活动类型数据
async function fetchTypeData22() {
    try {
        const response = await fetch('http://192.168.1.43:8080/activities/typeCounts');
        if (!response.ok) throw new Error('网络响应错误'); // 错误处理
        const result = await response.json();

        if (result.code === 200) {
            // 更新网页中对应的类别项
            const typeCounts = result.data; // 获取数据对象

            // 将类型数据填充到相应的 span 元素
            document.getElementById('sports').textContent = typeCounts['体育'] || 0; // 填充体育类
            document.getElementById('science').textContent = typeCounts['学术'] || 0; // 填充学术类
            document.getElementById('art').textContent = typeCounts['艺术'] || 0; // 填充艺术类
            document.getElementById('table').textContent = typeCounts['桌游'] || 0; // 填充桌游类
            document.getElementById('game').textContent = typeCounts['手游'] || 0; // 填充手游类
            document.getElementById('app').textContent = typeCounts['端游'] || 0; // 填充端游类
            document.getElementById('travel').textContent = typeCounts['出行'] || 0; // 填充出行类
            document.getElementById('other').textContent = typeCounts['其他'] || 0; // 填充其他类
        } else {
            console.error('请求失败，返回信息:', result.message);
        }

    } catch (error) {
        console.error('获取数据出错:', error);
    }
}



// 用于渲染图表
function renderChart(id, type, data, options = {}) {
    new Chart(document.getElementById(id), {
        type: type,
        data: data,
        options: options
    });
}

function initialize() {
    refreshData(); // 初始化加载数据
    setInterval(refreshData, 3000); // 每3秒更新一次数据
    
}
window.onload = initialize;

 async function sum() {
    // 获取排名数据
    const rankingResponse = await fetch('http://192.168.1.43:8080/event/top5usernames');
    const rankingData = {
        labels: [], // 用户名
        datasets: [{
            label: '活动发布数',
            data: [], // 用户活动发布数
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
            barThickness: 15,
        }]
    };

    // 处理排名数据
    if (rankingResponse.ok) {
        const rankingItems = await rankingResponse.json();
        rankingItems.forEach(item => {
            rankingData.labels.push(item.username);
            rankingData.datasets[0].data.push(item.count);
        });
    } else {
        console.error('获取排名数据出错');
    }

    // 渲染第一个图表
    renderChart('rankingChart', 'bar', rankingData,options);

    // 获取用户活动发布数排行数据
    const ranking2Response = await fetchRankingData(); // 假设这个函数是单独获取的
    const ranking2Data = {
        labels: ranking2Response.map(user => user.username), // 假设包含一个用户名
        datasets: [{
            label: '评分',
            data: ranking2Response.map(user => user.reputation), // 假设包含一个评分
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
           
            borderWidth: 0.5,
        barThickness: 15, // 设置每个条形的宽度（如：10）

        barPercentage: 0.4, // 将条形宽度设置成某种比例，0.4 较窄
        categoryPercentage: 0.3 // 类别间的相对宽度，0.8 保持一定的距离
        }]
    };

    
    

    // 渲染第二个图表
    renderChart('typeStatsChart', 'bar', ranking2Data,options);

    // 获取其他数据以及渲染其它图表
    const peopleDataResponse = await fetchPeopleData();
    const peopleData = {
        labels: Object.keys(peopleDataResponse),
        datasets: [{
            data: Object.values(peopleDataResponse),
            backgroundColor: ['#ffaa00', '#00aaff', '#00ff7f', '#ff00aa',],
        }]
    };

    const typeDataResponse = await fetchTypeData();
    const typeData = {
        labels: Object.keys(typeDataResponse),
        datasets: [{
            data: Object.values(typeDataResponse),
            backgroundColor: ['#ffaa00', '#00aaff', '#00ff7f', '#0033ff', '#ff00aa', '#ffcc00', '#0aa440', '#f44336'],
        }]
    };

    const statusDataResponse = await fetchStatusData();
    const statusData = {
        labels: Object.keys(statusDataResponse),
        datasets: [{
            data: Object.values(statusDataResponse),
            backgroundColor: ['#ffaa00', '#00aaff', '#00ff7f', '#0033ff'],
        }]
    };

    const dateDataResponse = await fetchDateData();
    const dateData = {
        labels: Object.keys(dateDataResponse),
        datasets: [{
            label: '活动数',
            data: Object.values(dateDataResponse),
            backgroundColor: ['#ffaa00', '#00aaff', '#00ff7f', '#0033ff', '#ff00aa', '#f44336', '#0aa440',],
        }]
    };

    // 渲染其它图表
    renderChart('trendChart', 'pie', peopleData,options3);
    renderChart('machineVsHumanChart', 'doughnut', typeData,options3);
    renderChart('distributionChart', 'pie', statusData,options3);
    renderChart('typeStatsChart2', 'bar', dateData,options2);
};

// 获取用户活动发布数排行数据
async function fetchRankingData() {
    const response = await fetch('http://192.168.1.43:8080/auth/top5Reputation');
    const result = await response.json();
    return result.data; // 假设 result.data 为数组形式
}

// 获取活动人数分布数据
async function fetchPeopleData() {
    const response = await fetch('http://192.168.1.43:8080/activities/maxceilingcounts');
    const result = await response.json();
    return result.data; // 确保返回的数据为符合需要的格式
}

// 获取活动类型数据
async function fetchTypeData() {
    const response = await fetch('http://192.168.1.43:8080/activities/typeCounts');
    const result = await response.json();
    console.log(result.data);
    return result.data;
}

// 获取活动状态数据
async function fetchStatusData() {
    const response = await fetch('http://192.168.1.43:8080/activities/statusCounts');
    const result = await response.json();
    return result.data;
}

// 获取近期活动数量数据
async function fetchDateData() {
    const response = await fetch('http://192.168.1.43:8080/activities/countlast7days');
    const result = await response.json();
    return result;
}







//刷新
async function refreshData() {
    try {
        // 总用户
        const userResponse = await fetch('http://192.168.1.43:8080/auth/count');
        const userData = await userResponse.json();
        if (userData.code === 200) {
            document.querySelector('.total-people').textContent = userData.data;
        } else {
            console.error('请求失败，返回信息:', userData.message);
        }

        // 活动总量
        const activityResponse = await fetch('http://192.168.1.43:8080/activities/countActivity');
        const activityData = await activityResponse.json();
        if (activityData.code === 200) {
            document.querySelector('.total-number').textContent = activityData.data;
        } else {
            console.error('请求失败，返回信息:', activityData.message);
        }

        // 分类数量
        await fetchTypeData22(); // 你原本的函数可以直接调用
        await sum(); // 你原本的函数可以直接调用
    } catch (error) {
        console.error('网络请求出现错误:', error);
    }
}


